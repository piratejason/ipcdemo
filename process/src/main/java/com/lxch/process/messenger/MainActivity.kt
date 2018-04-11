package com.lxch.process.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.lxch.process.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var bound = false
    var messager: Messenger? = null
    var replayMessenger: Messenger = Messenger(object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                1 -> {
                    var service = msg.data.getString("from service")
                    Toast.makeText(applicationContext, service, Toast.LENGTH_LONG).show()
                }
            }
        }
    })
    private var connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            messager = null
            bound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messager = Messenger(service)
            bound = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addBook.setOnClickListener { sayHello() }
    }

    override fun onStart() {
        super.onStart()
        var intent = Intent()
        intent.component = ComponentName("com.lxch.processmessenger", "com.lxch.processmessenger.MessengerService")
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun sayHello() {
        if (bound) {
            var msg = Message.obtain(null, 1)
            msg.replyTo = replayMessenger
            var b = Bundle()
            b.putString("from client", "fromclient")
            msg.data = b
            messager?.send(msg)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bound)
            unbindService(connection)
        bound = false
    }
}
