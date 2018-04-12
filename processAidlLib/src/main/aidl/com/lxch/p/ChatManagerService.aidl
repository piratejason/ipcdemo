// ChatManagerService.aidl
package com.lxch.p;

// Declare any non-default types here with import statements
import com.lxch.p.ChatManagerCallback;

interface ChatManagerService {
    void registerChatManager(ChatManagerCallback cb);
    void unregisterChatManager(ChatManagerCallback cb);
    void join(IBinder token,String name);
    void leave(IBinder token);
    List<String>getChatList();
}
