// ICrashExit.aidl
package com.lxch.p;

// Declare any non-default types here with import statements

interface ICrashExit {
    void join(IBinder token,String name);
    void leave(IBinder token);
    List<String> getJoined();
}
