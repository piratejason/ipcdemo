package com.lxch.process.aidl.chat

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lxch.process.R

class ChatAdapter(var context: Context) : RecyclerView.Adapter<ChatAdapter.Hold>() {
    var list= ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Hold {
        return Hold(View.inflate(context, R.layout.item_chat, null))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Hold, position: Int) {
        holder.tv.text = list[position]
    }

    inner class Hold(view: View) : RecyclerView.ViewHolder(view) {
        var tv = view.findViewById<TextView>(R.id.tv)
    }
}