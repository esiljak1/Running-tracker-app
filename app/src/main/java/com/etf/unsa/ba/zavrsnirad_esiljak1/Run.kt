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
    var endOfTheRun: String? = null
        get(){
            return field
        }
    var speedSamples: ArrayList<Float>? = null
        get() {
            return field
        }

    constructor() : this(0f, 0f, 0, ArrayList())

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
        val date = LocalDateTime.now()
        this.endOfTheRun = date.toLocalDate().toString() + "\n" + timeOfDay(date)
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

    private fun timeOfDay(date: LocalDateTime): String{
        if(date.hour in 5..12){
            return "Morning run"
        }else if(date.hour in 13..18) {
            return "Afternoon run"
        }

        return "Evening run"
    }
}