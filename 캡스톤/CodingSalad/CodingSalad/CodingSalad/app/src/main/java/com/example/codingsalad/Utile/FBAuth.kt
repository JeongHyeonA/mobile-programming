package com.example.codingsalad.Utile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

//firebase 사용하기 위해 함수를 만들어주는 패키지와 클레스

class FBAuth {

    companion object {

        private lateinit var auth : FirebaseAuth

        fun getUid() : String{       //uid 값을 가져오기 위함 , 현재 사용자를 uid 라고 함
            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.email.toString()  //현재 uid 를 리턴해줌
        }


        fun getTime() : String{     //uid 값 가져오는것 처럼 시간을 가져옴 , 현재 시각

            val currentDataTime = Calendar.getInstance().time
            val dataFormat = SimpleDateFormat("yyyy,MM,dd HH:mm:ss", Locale.KOREA).format(currentDataTime) // 현재 시간을 가져오는 코딩
                                                                          //Loclae란 어떤 장소의 시간을 가져올 것이냐
            return dataFormat

        }
    }

}