package com.lxch.p

import android.os.Parcel
import android.os.Parcelable

class Book() : Parcelable {
    var id = 0
    var name = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}