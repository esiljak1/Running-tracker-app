package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.location.Location
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.LocationResult

interface MapUIInterface {
    fun updateUIValues(location:  Location)
    fun postLocationCallback(locationResult: LocationResult){
        return
    }
    fun get(): FragmentActivity?
    fun requestPermissions()
}