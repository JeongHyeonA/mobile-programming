package com.example.codingsalad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.codingsalad.Category.CategoryFragment
import com.example.codingsalad.TalkFragment.SudaFragment
import com.example.codingsalad.chatlist.ChatListFragment
import com.example.codingsalad.home.HomeFragment
import com.example.codingsalad.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val sudaFragment = SudaFragment()
        val chatListFragment = ChatListFragment()
        val categoryFragment = CategoryFragment()
        val myPageFragment = MyPageFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> replaceFragment(homeFragment)
                R.id.category -> replaceFragment(categoryFragment)
                R.id.chatList -> replaceFragment(chatListFragment)
                R.id.board -> replaceFragment(sudaFragment)
                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction()
            .apply{
                replace(R.id.fragmenContainer, fragment)
                commit()
            }
    }

}