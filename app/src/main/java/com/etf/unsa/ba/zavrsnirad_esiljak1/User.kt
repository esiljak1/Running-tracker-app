package com.etf.unsa.ba.zavrsnirad_esiljak1

import java.util.*

class User {
    var uuid: UUID? = null
        get() {
            return field
        }
    var userName: String = ""
        get(){
            return field
        }
        set(value) {
            field = value
        }
    var mail: String = ""
        get(){
            return field
        }
        set(value) {
            field = value
        }
}