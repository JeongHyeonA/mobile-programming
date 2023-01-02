package com.example.codingsalad.Board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.codingsalad.Model.BoardModel
import com.example.codingsalad.R
import com.example.codingsalad.Utile.FBAuth
import com.example.codingsalad.Utile.FBRef
import com.example.codingsalad.databinding.ActivityBoardWriteBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardWriteBinding

    private val TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false

    val storage = Firebase.storage

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //바인딩 만들기 2
        binding = ActivityBoardWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.writeBtn.setOnClickListener {

            val title = binding.titleArea.text.toString()  //write 버튼 클릭시 타이틀 에어리어 즉 위에 잇는 내용을 얻어옴
            val content = binding.contentArea.text.toString()  // write 클릭시 컨텐트 에어리어 즉 아래 있는 내용을 얻어옴
            val uid = FBAuth.getUid()   //FBAuth에 있는 함수로 uid를 가져옴
            val time = FBAuth.getTime()    //FBAuth에 있는 함수로 time를 가져옴

            Log.d(TAG, title)
            Log.d(TAG, content)

            // 파이어 베이스에 store에 이미지를 저장하고 싶습니다

            //만약 내가 게시글을 클릭했을 때, 게시글에 대한 정보를 받아와야 하는데
            //이미지 이름에 대한 정보를 모르기 때문에
            //이미지 이름을 문서의 key값으로 해줘서 이미지에 대한 정보를 찾기 쉽게 해둠


            val key = FBRef.boardRef.push().key.toString()

            FBRef.boardRef //FBRef 로 아이디 값을 얻어 오고 FBAuth 에 uid 를 넣어줌
                .child(key)   //랜덤으로 들어가고
                .setValue(
                    BoardModel(
                        title,
                        content,
                        uid,
                        time,
                    )
                )   //boardmodel은 자바 클래스 확인하면  boardModel ( title, content, uid (쓴사람), time) 이값들을 저장해두어 이런 형태로 들어가게돼

            Toast.makeText(this, "게시글 입력 완료 ", Toast.LENGTH_LONG).show()

            if (isImageUpload == true) {
                imageUpload(key)
            }


            finish()


        }

        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }


    }

    private fun imageUpload(key: String) {
        // Get the data from an ImageView as bytes

        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
        }

    }
}