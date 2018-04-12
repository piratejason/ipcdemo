package com.lxch.processaidl.chat

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.widget.Toast
import com.lxch.p.ChatManagerCallback
import com.lxch.p.ChatManagerService

private var list = HashMap<IBinder, ChatService.Client>()
private var callbacks = RemoteCallbackList<ChatManagerCallback>()

class ChatService : Service() {
    private var binder = object : ChatManagerService.Stub() {
        override fun getChatList(): MutableList<String> {
            var chatList = ArrayList<String>()
            list.map { chatList.add(it.value.name) }
            return chatList
        }

        override fun registerChatManager(cb: ChatManagerCallback?) {
            callbacks.register(cb)
        }

        override fun unregisterChatManager(cb: ChatManagerCallback?) {
            callbacks.unregister(cb)
        }

        override fun join(token: IBinder?, name: String?) {
            token?.let {
                val get = list[token]
                if (get == null) {
                    name?.let {
                        val client = Client(token, it)
                        token.linkToDeath(client, 0)
                        list[token] = client
                        notify(name, true)
                    }
                } else {
                    Toast.makeText(applicationContext, "already joined", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun leave(token: IBinder?) {
            val client = list[token]
            if (client == null) {
                Toast.makeText(applicationContext, "already leave", Toast.LENGTH_SHORT).show()
            } else {
                list.remove(token)
                notify(client.name, false)
            }
        }
    }

    private fun notify(name: String, isJoin: Boolean) {
        var beginBroadcast = callbacks.beginBroadcast()
        while (beginBroadcast > 0) {
            beginBroadcast--
            callbacks.getBroadcastItem(beginBroadcast).onAction(name, isJoin)
        }
        callbacks.finishBroadcast()
    }

    class Client(var token: IBinder, var name: String) : IBinder.DeathRecipient {
        override fun binderDied() {
            val client = list[token]
            if (client != null) {
                list.remove(token)
                var beginBroadcast = callbacks.beginBroadcast()
                while (beginBroadcast > 0) {
                    beginBroadcast--
                    callbacks.getBroadcastItem(beginBroadcast).onAction(name, false)
                }
                callbacks.finishBroadcast()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        callbacks.kill()
        super.onDestroy()
    }
}