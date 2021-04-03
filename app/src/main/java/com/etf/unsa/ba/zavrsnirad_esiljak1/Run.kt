package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDateTime

class Run(distanceMeters: Float, topSpeed: Float, durationSeconds: Long, speedSamples: ArrayList<Float>) : Parcelable {
    var distanceMeters: Float = 0f
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
    var speedSamples: ArrayList<Float>? = null
        get() {
            return field
        }

    constructor(parcel: Parcel) : this(
            TODO("distance"),
            TODO("topSpeed"),
            TODO("durationSeconds"),
            TODO("speedSamples")) {
    }

    init{
        this.distanceMeters = distanceMeters
        this.topSpeed = topSpeed
        this.durationSeconds = durationSeconds
        this.speedSamples = speedSamples
        this.endOfTheRun = LocalDateTime.now()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Run> {
        override fun createFromParcel(parcel: Parcel): Run {
            return Run(parcel)
        }

        override fun newArray(size: Int): Array<Run?> {
            return arrayOfNulls(size)
        }
    }
}