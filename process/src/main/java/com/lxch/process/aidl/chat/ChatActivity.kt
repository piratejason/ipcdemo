package com.lxch.process.aidl.chat

import android.os.Binder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.lxch.p.ChatManagerCallback
import com.lxch.process.R
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private var bind = Binder()
    private var adapter=ChatAdapter(this)
    private var callback=object:ChatManagerCallback.Stub(){
        override fun onAction(name: String?, toJoin: Boolean) {
            adapter.list.add(name!!)
            adapter.notifyDataSetChanged()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        ChatManager.connect()
        removeClient.isEnabled = false
        addClient.setOnClickListener {
            if (ChatManager.isConnecting) {
                ChatManager.getService()!!.join(bind, "asdfasd")
                ChatManager.getService()!!.registerChatManager(callback)
                removeClient.isEnabled = true
                addClient.isEnabled = false
            }
        }
        removeClient.setOnClickListener {
            if (ChatManager.isConnecting) {
                ChatManager.getService()!!.leave(bind)
                ChatManager.getService()!!.unregisterChatManager(callback)
                adapter.list.clear()
                adapter.notifyDataSetChanged()
                removeClient.isEnabled = false
                addClient.isEnabled = true
            }

        }
        unbind.setOnClickListener { ChatManager.disConnect() }
        getList.setOnClickListener {
            if (ChatManager.isConnecting) {
                load()
            }
        }
        smartRL.setOnRefreshListener {
            if (ChatManager.isConnecting) {
                load()
            }
            smartRL.finishRefresh(2000)
        }
        smartRL.setOnLoadMoreListener {
            if (ChatManager.isConnecting) {
                load()
            }
            smartRL.finishLoadMore(2000)
        }
    }

    private fun load() {
        val chatList = ChatManager.getService()!!.chatList
        var manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.list= chatList as ArrayList<String>
        rv.layoutManager = manager
        rv.adapter =adapter

    }
}