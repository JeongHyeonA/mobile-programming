package com.example.codingsalad.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.codingsalad.Model.BoardModel
import com.example.codingsalad.R
import com.example.codingsalad.Utile.FBAuth
import com.example.codingsalad.Utile.FBRef
import com.example.codingsalad.databinding.ActivityBoardEditBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardEditActivity : AppCompatActivity() {

    private var cate = ""
    private lateinit var key: String

    private lateinit var binding: ActivityBoardEditBinding

    private val TAG = BoardEditActivity::class.java.simpleName

    private lateinit var writerUid: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)



        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }

    }

    private fun editBoardData(key: String) {

        FBRef.boardRef //FBRef 로 아이디 값을 얻어 오고 FBAuth 에 uid 를 넣어줌
            .child(key)   //랜덤으로 들어가고
            .setValue(
                BoardModel(
                    binding.titleArea.text.toString(),
                    binding.contentArea.text.toString(),
                    writerUid,
                    FBAuth.getTime(),
                )
            )
        Toast.makeText(this, "수정완료", Toast.LENGTH_LONG).show()

        finish()

    }

    private fun getImageData(key: String) {

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            } else {


            }
        })

    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {    //컨텐츠 아이디 값을 받아오고 있음 firebase에 컨텐츠들을 저장
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.content)
                writerUid = dataModel!!.uid


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }

        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

}



