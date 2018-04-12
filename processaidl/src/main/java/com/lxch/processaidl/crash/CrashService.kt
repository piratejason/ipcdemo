package com.lxch.processaidl.crash

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.lxch.p.ICrashExit

private var list = HashMap<IBinder, CrashService.Client>()

class CrashService : Service() {
    private var binder = object : ICrashExit.Stub() {

        override fun join(token: IBinder?, name: String?) {
            token?.let {
                if (findClient(it)) {
                    Toast.makeText(applicationContext, "already joined", Toast.LENGTH_SHORT).show()
                } else {
                    name?.let {
                        val client = Client(token, name)
                        token.linkToDeath(client, 0)
                        list.put(token, client)
                    }
                }
            }
        }

        override fun leave(token: IBinder?) {
            token?.let {
                if (!findClient(token)) {
                    Toast.makeText(applicationContext, "already leave", Toast.LENGTH_SHORT).show()
                } else {
                    val get = list.get(token)
                    list.remove(token)
                    get?.let { it.token.unlinkToDeath(get, 0) }
                }
            }
        }

        override fun getJoined(): MutableList<String> {
            var names = ArrayList<String>()
            list.map { entry -> names.add(entry.value.name) }
            return names
        }

    }

    private fun findClient(token: IBinder): Boolean {
        val filter = list.filter { client -> client.key == token }
        return filter.isNotEmpty()
    }

    class Client(var token: IBinder, var name: String) : IBinder.DeathRecipient {

        override fun binderDied() {
            val indexOf = list.get(token)
            if (indexOf != null) {
                list.remove(token)
                Log.e("clientcrash", "crash")
            }
        }

    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}