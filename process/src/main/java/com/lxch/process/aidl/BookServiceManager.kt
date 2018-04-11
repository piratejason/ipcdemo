package com.lxch.process.aidl

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.lxch.p.IBookManager

class BookServiceManager(private var context: Context) {
    private var service: IBookManager? = null
    private var isConnecting = false

    companion object {
        private var instance: BookServiceManager? = null
        fun instance(context: Context): BookServiceManager {
            if (instance == null) {
                instance = BookServiceManager(context)
            }
            return instance as BookServiceManager
        }
    }

    fun getBookService(): IBookManager? {
        return service as IBookManager
    }

    fun connectService() {
        if (!isConnecting) {
            var inn = createIntent()
            isConnecting = context.bindService(Intent(inn), connection, Service.BIND_AUTO_CREATE)
        }
    }

    private var connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
        }

        override fun onServiceConnected(name: ComponentName?, ser: IBinder?) {
            service = IBookManager.Stub.asInterface(ser)
            if (context is ConnectCallback) {
                val connectCallback = context as ConnectCallback
                connectCallback.success()
            }
        }

    }

    private fun createIntent(): Intent? {
        val inn = Intent("bookservice")
        var pm = context.packageManager
        var resolveInfo = pm.queryIntentServices(inn, 0)
        if (resolveInfo == null || resolveInfo.size != 1)
            return null
        var ri = resolveInfo[0]
        var pack = ri.serviceInfo.packageName
        var cl = ri.serviceInfo.name
        var intent = Intent(inn)
        intent.component = ComponentName(pack, cl)
        return intent
    }
}