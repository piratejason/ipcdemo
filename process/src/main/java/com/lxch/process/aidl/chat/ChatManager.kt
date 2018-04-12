package com.lxch.process.aidl.chat

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.lxch.p.ChatManagerService
import com.lxch.process.App

object ChatManager {
    private var manager: ChatManagerService? = null
    var isConnecting = false
    private var instance: ChatManager? = null
    fun getInstance(): ChatManager? {
        if (instance == null)
            create()
        return instance
    }

    @Synchronized
    private fun create() {
        instance = ChatManager
    }

    private var connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            manager = null
            isConnecting = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            manager = ChatManagerService.Stub.asInterface(service)
        }
    }

    fun connect() {
        if (!isConnecting) {
            val intent = createIntent()
            isConnecting = App.instance()!!.bindService(intent, connection, Service.BIND_AUTO_CREATE)
        }
    }

    fun disConnect() {
        if (isConnecting) {
            App.instance()!!.unbindService(connection)
        }
    }

    fun getService(): ChatManagerService? {
        return manager
    }

    fun createIntent(): Intent? {
        var find = Intent("ChatService")
        val services = App.instance()!!.packageManager.queryIntentServices(find, 0)
        if (services.isNotEmpty()) {
            val resolveInfo = services[0]
            var a = Intent(find)
            a.component = ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name)
            return a
        } else {
            return null
        }
    }
}