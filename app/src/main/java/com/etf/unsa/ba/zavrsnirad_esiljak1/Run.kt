package com.etf.unsa.ba.zavrsnirad_esiljak1

import java.time.LocalDateTime

class Run(user: User, distance: Float, topSpeed: Float, durationSeconds: Long) {
    var user: User? = null
        get(){
            return field
        }
    var distance: Float = 0f
        get() {
            return field
        }
    var topSpeed: Float = 0f
        get() {
            return field
        }
    var durationSeconds: Long = 0
        get() {
            return field
        }
    var endOfTheRun: LocalDateTime? = null
        get(){
            return field
        }

    init{
        this.user = user
        this.distance = distance
        this.topSpeed = topSpeed
        this.durationSeconds = durationSeconds
        this.endOfTheRun = LocalDateTime.now()
    }
}