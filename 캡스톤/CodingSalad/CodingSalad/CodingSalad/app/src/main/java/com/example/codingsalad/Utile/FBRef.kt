package com.example.codingsalad.Utile

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// contentList에서 값을 한개씩 다 넣어주기 귀찮으니 함수를 만듬

class FBRef {

    companion object {

        private val database = Firebase.database


        val category1 = database.getReference("contents")
        val category2 = database.getReference("contents2")
        val bookmarkRef = database.getReference("bookmark_list")
        val boardRef = database.getReference("board")   //위에있는것도 전부 realtime database에 목록을 만들어줌
        val commentRef = database.getReference("comment")


    }

}