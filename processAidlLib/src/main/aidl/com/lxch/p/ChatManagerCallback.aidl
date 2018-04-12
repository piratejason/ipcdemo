// ChatManagerCallback.aidl
package com.lxch.p;

// Declare any non-default types here with import statements

interface ChatManagerCallback {
    void onAction(String name,boolean toJoin);
}
