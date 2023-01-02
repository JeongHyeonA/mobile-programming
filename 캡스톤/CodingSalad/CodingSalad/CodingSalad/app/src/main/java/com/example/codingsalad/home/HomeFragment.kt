package com.example.codingsalad.home

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codingsalad.DBKey.Companion.CHILD_CHAT
import com.example.codingsalad.DBKey.Companion.DB_ARTICLES
import com.example.codingsalad.DBKey.Companion.DB_USERS
import com.example.codingsalad.R
import com.example.codingsalad.chatlist.ChatListItem
import com.example.codingsalad.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var articlAdapter: ArticleAdapter
    private var searchToken:Boolean = false
    private lateinit var userDB: DatabaseReference
    private lateinit var articleDB: DatabaseReference
    private lateinit var searchtext: String
    private val articleList = mutableListOf<ArticleModel>()
    private val searchList = mutableListOf<ArticleModel>()
    private lateinit var binding: FragmentHomeBinding

    private val mChildListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            // DB에 article이 추가될때마다 동작하는 리스너
            // article 자체를 객체를 통해서 주고받음
            val articleModel = snapshot.getValue(ArticleModel::class.java)

            Log.d(TAG, "addChildEventListener is Called!!")
            articleModel ?: return // null시 반환
            if(!searchToken) {
                articleList.add(articleModel)
                articlAdapter.submitList(articleList)
                articlAdapter.notifyDataSetChanged()
            }
            else {
                if (articleModel?.title?.contains(searchtext)){
                    articleList.add(articleModel)
                    articlAdapter.notifyDataSetChanged()//submitList(articleList)
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}

    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser
        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding

        articleList.clear()
        userDB = Firebase.database.reference.child(DB_USERS)
        articleDB = Firebase.database.reference.child(DB_ARTICLES)

        val btnSearch = binding.btnSearch

        articlAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if (user != null) {
                //로그인 한 상태
                if (auth.currentUser?.uid != articleModel.sellerId) {

                    val chatRoom = ChatListItem(
                        buyerId = user.uid,
                        sellerId = articleModel.sellerId,
                        itemTitle = articleModel.title,
                        key = System.currentTimeMillis()
                    )

                    userDB.child(user.uid)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    userDB.child(articleModel.sellerId)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    Snackbar.make(view, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.", Snackbar.LENGTH_LONG).show()

                } else {
                    //내가 올린 아이템
                    Snackbar.make(view, "내가 올린 아이템입니다.", Snackbar.LENGTH_LONG).show()
                }

            } else {
                //로그인 안한 상태
                Snackbar.make(view, "로그인 상태를 확인해주세요", Snackbar.LENGTH_LONG).show()
            }
        })

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articlAdapter

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            context?.let {

                if (auth.currentUser != null) {
                    val intent = Intent(requireContext(), AddArticleActivity::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(view, "로그인 상태를 확인해 주세요", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        articleDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                articleList.clear()
                Log.d(TAG, "addListenerForSingleValueEvent is Called!!")
                articleDB.addChildEventListener(mChildListener)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        btnSearch.setOnClickListener {
            dialog()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView!!")
        articleList.clear()
        Log.d(TAG, "list is cleared!! now size : ${articleList.size}")
        articleDB.removeEventListener(mChildListener)
    }

    private fun dialog() {
        val builder = AlertDialog.Builder(this.context)
        val et: EditText = EditText(this.context)
        builder.apply {
            setTitle("검색")
            setMessage("검색할 내용을 입력해주세요.")
            setView(et)
            setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    searchtext = et.text.toString() //이 값으로 db에서 검색
                    searchToken = true
                    slist(searchtext)
                })
            setNeutralButton("초기화",
                DialogInterface.OnClickListener{ dialog, id ->
                    searchToken = false
                    Log.d("초기화", "눌림")
                    slist("null")

                }
            )
            setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                })
            // 다이얼로그를 띄워주기
            builder.show()
        }
    }

    private fun slist(text: String) {
        articleList.clear()
        articleDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                articleDB.addChildEventListener(mChildListener)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}