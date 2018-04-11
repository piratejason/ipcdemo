package com.lxch.process.aidl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.lxch.p.Book
import com.lxch.process.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ConnectCallback {
    override fun success() {
        Toast.makeText(this, "success", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BookServiceManager.instance(this).connectService()
        plus.setOnClickListener {
            val bookService = BookServiceManager.instance(this).getBookService()
            if (bookService == null) {
                Toast.makeText(this, "尚未连接服务", Toast.LENGTH_LONG).show()
            } else {
                val add = bookService.add(12)
                Toast.makeText(this, add.toString(), Toast.LENGTH_LONG).show()

            }
        }
        addBook.setOnClickListener {
            val bookService = BookServiceManager.instance(this).getBookService()
            if (bookService == null) {
                Toast.makeText(this, "尚未连接服务", Toast.LENGTH_LONG).show()
            } else {
                var b = Book()
                b.id = 11
                b.name = "11"
                bookService.addBook(b)
            }
        }
        getBookList.setOnClickListener {
            val bookService = BookServiceManager.instance(this).getBookService()
            if (bookService == null) {
                Toast.makeText(this, "尚未连接服务", Toast.LENGTH_LONG).show()
            } else {
                val size = bookService.bookList.size
                Toast.makeText(this, size.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}