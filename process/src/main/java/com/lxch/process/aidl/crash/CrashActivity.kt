package com.lxch.process.aidl.crash

import android.os.Binder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.lxch.process.R
import com.lxch.process.aidl.ConnectCallback
import kotlinx.android.synthetic.main.activity_main.*

class CrashActivity : AppCompatActivity(), ConnectCallback {
    override fun success() {
        Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
    }

    var token = Binder()
    var isJoined = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        plus.text = "join"
        addBook.text = "leave"
        getBookList.text = "size"
        CrashServiceManager.instance(this).connect()
        plus.setOnClickListener {
            val bookService = CrashServiceManager.instance(this).getService()
            if (bookService == null) {
                Toast.makeText(this, "尚未连接服务", Toast.LENGTH_LONG).show()
            } else if (!isJoined) {
                bookService.join(token, "name:11")
                isJoined = true
            }
        }
        addBook.setOnClickListener {
            val bookService = CrashServiceManager.instance(this).getService()
            if (bookService == null) {
                Toast.makeText(this, "尚未连接服务", Toast.LENGTH_LONG).show()
            } else {
               bookService.leave(token)
                isJoined=false
            }
        }
        getBookList.setOnClickListener {
            val bookService = CrashServiceManager.instance(this).getService()
            if (bookService == null) {
                Toast.makeText(this, "尚未连接服务", Toast.LENGTH_LONG).show()
            } else {
                val size = bookService.joined.size
                Toast.makeText(this, size.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}