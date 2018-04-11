package com.lxch.processaidl

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.widget.Toast
import com.lxch.p.Book
import com.lxch.p.IBookManager


class BookService : Service() {
    var d = 0
    var list = ArrayList<Book>()
    var h = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            Toast.makeText(applicationContext, "add book success", Toast.LENGTH_LONG).show()
        }
    }
    private var binder = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> {
            return list
        }

        override fun addBook(book: Book?) {
            book?.let { list.add(it) }
            h.sendEmptyMessage(0)
            Toast.makeText(applicationContext, "dd", Toast.LENGTH_LONG).show()
        }

        override fun add(a: Int): Int {
            d += a
            return d
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

}