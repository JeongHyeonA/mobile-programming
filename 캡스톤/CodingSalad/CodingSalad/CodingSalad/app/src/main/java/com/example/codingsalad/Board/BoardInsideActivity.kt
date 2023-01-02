package com.example.codingsalad.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.codingsalad.Adapter.CommentLVAdapter
import com.example.codingsalad.Model.BoardModel
import com.example.codingsalad.Model.CommentModel
import com.example.codingsalad.R
import com.example.codingsalad.Utile.FBAuth
import com.example.codingsalad.Utile.FBRef
import com.example.codingsalad.databinding.ActivityBoardInsideBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Exception

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding

    private lateinit var key:String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentAdapter : CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)

        binding.boardSettingIcon.setOnClickListener{
            showDialog()
        }



        // 첫번째 방법
//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//        binding.titleArea.text = title
//        binding.textArea.text = content
//        binding.timeArea.text = time

        // 두번째 방법

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)


        binding.commentBtn.setOnClickListener{
            insertComment(key)
        }

        getCommentData(key)

        commentAdapter = CommentLVAdapter(commentDataList)
        binding.commentLV.adapter = commentAdapter



    }

    fun getCommentData(key : String){
        val postListener = object : ValueEventListener {    //컨텐츠 아이디 값을 받아오고 있음 firebase에 컨텐츠들을 저장
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()
                for (DataModel in dataSnapshot.children){

                    val item = DataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }

                commentAdapter.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.commentRef.child(key).addValueEventListener(postListener)

    }

    fun insertComment(key : String){
        //comment
        //     - Boardkey
        //         - Commentkey
        //              - commentData
        //              - commentData
        //              - commentData
        val myUid = FBAuth.getUid()
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.commentArea.text.toString(),
                    FBAuth.getTime(),
                    myUid
                )
            )

        Toast.makeText(this,"댓글 입력 완료",Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")
    }

    private fun showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this).create()
        mBuilder.apply {
            setView(mDialogView)
            setTitle("게시글 수정/삭제")
        }.show()
        mBuilder.findViewById<Button>(R.id.editBtn)?.setOnClickListener {

            Toast.makeText(this, "수정 버튼을 눌렀습니다.", Toast.LENGTH_LONG).show()

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
            mBuilder.dismiss()
        }

        mBuilder.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_LONG).show()
            finish()
        }

    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            }else {
                binding.getImageArea.isVisible = false

            }
        })

    }

    private fun getBoardData(key : String){

        val postListener = object : ValueEventListener {    //컨텐츠 아이디 값을 받아오고 있음 firebase에 컨텐츠들을 저장
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try{
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG,dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textArea.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    val myUid = FBAuth.getUid()
                    val writerid = dataModel.uid

                    if(myUid.equals(writerid)){
                        Toast.makeText(baseContext,"내가 쓴 게시글",Toast.LENGTH_LONG).show()
                        binding.boardSettingIcon.isVisible = true

                    }else{
                        Toast.makeText(baseContext,"다른 작성자의 글입니다.",Toast.LENGTH_LONG).show()
                    }


                }catch (e : Exception){

                    Log.d(TAG, "삭제완료")

                }



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)

    }


}