package com.lxch.processmessenger

import android.app.Service
import android.content.Intent
import android.os.*
import android.widget.Toast

class MessengerService : Service() {

    var msgs = Messenger(object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                1 -> {
                    var client = msg.data.getString("from client")
                    Toast.makeText(applicationContext, client, Toast.LENGTH_SHORT).show()
                    var b=Bundle()
                    b.putString("from service","got")
                    msg.what=1
                    msg.data=b
                    msg.replyTo.send(msg)
                }
            }
        }
    })

    override fun onBind(intent: Intent?): IBinder {
        return msgs.binder
    }
}