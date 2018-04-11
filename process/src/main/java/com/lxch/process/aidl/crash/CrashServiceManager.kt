package com.lxch.process.aidl.crash

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.lxch.p.ICrashExit
import com.lxch.process.aidl.ConnectCallback

class CrashServiceManager(private var context: Context) {
    private var serviceManager: ICrashExit? = null
    private var isConnecting = false

    companion object {
        private var instance: CrashServiceManager? = null
        fun instance(context: Context): CrashServiceManager {
            if (instance == null)
                instance = CrashServiceManager(context)
            return instance as CrashServiceManager
        }
    }

    fun getService(): ICrashExit? {
        return serviceManager
    }

    fun connect() {
        if (!isConnecting) {
            val createIntent = createIntent()
            isConnecting = context.bindService(createIntent, connect, Service.BIND_AUTO_CREATE)
        }
    }

    private var connect = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            serviceManager = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceManager = ICrashExit.Stub.asInterface(service)
            if (context is ConnectCallback) {
                var back = context as ConnectCallback
                back.success()
            }
        }

    }

    private fun createIntent(): Intent? {
        val inn = Intent("crashservice")
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