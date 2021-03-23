package com.etf.unsa.ba.zavrsnirad_esiljak1

import java.util.*

class User(uuid: UUID, userName: String, mail: String) {
    var uuid: UUID? = null
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

    init {
        this.uuid = uuid
        this.userName = userName
        this.mail = mail
    }
}