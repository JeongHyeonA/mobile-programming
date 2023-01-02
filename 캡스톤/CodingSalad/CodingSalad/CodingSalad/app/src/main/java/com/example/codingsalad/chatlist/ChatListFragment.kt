package com.example.codingsalad.chatlist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codingsalad.DBKey.Companion.CHILD_CHAT
import com.example.codingsalad.DBKey.Companion.DB_USERS
import com.example.codingsalad.R
import com.example.codingsalad.chatdetail.ChatRoomActivity
import com.example.codingsalad.databinding.FragmentChatlistBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment: Fragment(R.layout.fragment_chatlist) {

    private lateinit var chatDB: DatabaseReference
    private var binding: FragmentChatlistBinding? = null
    private  lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()
    val user = Firebase.auth.currentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatlistBinding = FragmentChatlistBinding.bind(view)
        binding = fragmentChatlistBinding

        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            //채팅방으로 이동 하는 코드
            context?.let {
                val intent = Intent(it,ChatRoomActivity::class.java)
                intent.putExtra("chatKey",chatRoom.key)
                startActivity(intent)
            }
        })

        chatRoomList.clear()

        fragmentChatlistBinding.chatListRecyclerView.adapter = chatListAdapter
        fragmentChatlistBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        if(user == null){
            return
        }

        val chatDB = Firebase.database.reference.child(DB_USERS).child(user.uid).child(CHILD_CHAT)

        chatDB.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }

                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }


    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }
}