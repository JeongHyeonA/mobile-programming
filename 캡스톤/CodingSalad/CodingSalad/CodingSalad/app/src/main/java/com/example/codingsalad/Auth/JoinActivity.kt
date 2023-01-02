package com.example.codingsalad.Auth

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.codingsalad.MainActivity
import com.example.codingsalad.R
import com.example.codingsalad.databinding.ActivityJoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class JoinActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityJoinBinding

    private var imageUri : Uri? = null

    //이미지 등록
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if(result.resultCode == RESULT_OK) {
                imageUri = result.data?.data //이미지 경로 원본
                binding.addphoto.setImageURI(imageUri) //이미지 뷰를 바꿈
                Log.d("이미지", "성공")
            }
            else{
                Log.d("이미지", "실패")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_join)

        auth = Firebase.auth

        database = Firebase.database.reference

        binding.addphoto.setOnClickListener{
            val intentImage = Intent(Intent.ACTION_PICK)
            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)
        }

        binding.joinBtn.setOnClickListener {

            var isGoToJoin = true

            val email = binding.emailArea.text.toString()
            val password1 = binding.passwordArea1.text.toString()
            val nickname = binding.nicknameArea.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this,"이메일을 입력해주세요.",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if(password1.isEmpty()){
                Toast.makeText(this,"비밀번호를 입력해주세요.",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }
            if(password1.length < 6){
                Toast.makeText(this,"비밀번호를 6자리 이상으로 입력해주세요",Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(isGoToJoin) {
                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_LONG).show()

                            val user = Firebase.auth.currentUser
                            val userId = user?.uid
                            val userIdSt = userId.toString()

                            FirebaseStorage.getInstance()
                                .reference.child("userImages").child("$userIdSt/photo")
                                .putFile(imageUri!!).addOnSuccessListener {
                                    var userProfile: Uri? = null
                                    FirebaseStorage.getInstance().reference.child("userImages")
                                        .child("$userIdSt/photo").downloadUrl
                                        .addOnSuccessListener {
                                            userProfile = it
                                            Log.d("이미지 URL", "$userProfile")
                                            val friend = UserDB(
                                                email.toString(),
                                                nickname.toString(),
                                                userProfile.toString(),
                                                userIdSt
                                            )
                                            database.child("users").child(userId.toString())
                                                .setValue(friend)

                                            val intent = Intent(this, MainActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        }
                                }
                        } else
                            Toast.makeText(this, "회원 가입 실패", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}