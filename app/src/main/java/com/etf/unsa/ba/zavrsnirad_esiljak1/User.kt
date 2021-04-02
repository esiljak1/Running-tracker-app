package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.os.Parcel
import android.os.Parcelable

class User(uuid: String?, userName: String, mail: String) : Parcelable{
    var uuid: String? = null
        get() {
            return field
        }
    var userName: String = ""
        get(){
            return field
        }
    var mail: String = ""
        get(){
            return field
        }

    constructor(parcel: Parcel) : this(
            TODO("uuid"),
            TODO("userName"),
            TODO("mail")) {
    }

    init {
        this.uuid = uuid
        this.userName = userName
        this.mail = mail
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}