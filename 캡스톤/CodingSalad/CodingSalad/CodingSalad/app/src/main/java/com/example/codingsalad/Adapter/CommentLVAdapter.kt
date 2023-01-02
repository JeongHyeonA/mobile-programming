package com.example.codingsalad.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.codingsalad.Model.CommentModel
import com.example.codingsalad.R

class CommentLVAdapter (val commentList : MutableList<CommentModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return commentList.size
    }

    override fun getItem(p0: Int): Any {
        return commentList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1

        if (view ==null) {
            view = LayoutInflater.from(p2?.context).inflate(R.layout.board_list_item, p2, false)
        }
        val comment = view?.findViewById<TextView>(R.id.contentArea)
        val title = view?.findViewById<TextView>(R.id.titleArea)
        val time = view?.findViewById<TextView>(R.id.timeArea)

        title!!.text = commentList[p0].commentTitle
        time!!.text = commentList[p0].commentCreatedTime
        comment!!.text = commentList[p0].commentWriter
        return view!!
    }
}
