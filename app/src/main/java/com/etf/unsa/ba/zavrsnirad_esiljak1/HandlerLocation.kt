package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

//TODO Add restarting of app if the app just got the permission for using location
class HandlerLocation private constructor() {
    private val locationInterval: Long = 5
    private val locationFastestInterval: Long = 2

    var ui: MapUIInterface? = null
            get() {
                return field
            }
            set(value) {
                field = value
            }
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val locationRequest = LocationRequest()
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            //ui!!.updateUIValues(p0.lastLocation)
            ui!!.postLocationCallback(p0)
        }
    }

    companion object{
        val instance = HandlerLocation()
    }

    fun start(){
        locationRequest.interval = 1000 * locationInterval
        locationRequest.fastestInterval = 1000 * locationFastestInterval

        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        updateGPS()
        startLocationUpdates()
    }

    fun updateGPS(){
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(ui!!.get()!!)

        if(ActivityCompat.checkSelfPermission(ui!!.get()!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProvider.lastLocation.addOnSuccessListener(
                    ui!!.get()!!
            ) { p0 ->
                if(p0 != null)
                    ui!!.updateUIValues(p0)
            }
        }else{
            ui!!.permissions()
        }
    }

    private fun startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(ui!!.get()!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ui!!.get()!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}