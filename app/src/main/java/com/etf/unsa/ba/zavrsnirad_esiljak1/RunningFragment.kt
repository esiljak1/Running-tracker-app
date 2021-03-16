package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ba.zavrsnirad_esiljak1.R
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import kotlin.math.abs


class RunningFragment : Fragment() {

    private val PERMISSION_FINE_LOCATION: Int = 99
    private val EPS: Double = 0.001
    private var totalDistance: Double = 0.0
    private var startTime: Long = 0

    private lateinit var tv_time: TextView
    private lateinit var tv_speed: TextView
    private lateinit var tv_latitude: TextView
    private lateinit var tv_longitude: TextView
    private lateinit var tv_distance: TextView

    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var previousLocation: Location
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_running, container, false)

        tv_time = view.findViewById(R.id.tv_time)
        tv_speed = view.findViewById(R.id.tv_speed)
        tv_latitude = view.findViewById(R.id.tv_latitude)
        tv_longitude = view.findViewById(R.id.tv_longitude)
        tv_distance = view.findViewById(R.id.tv_distance)

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                val currentLocation = p0.lastLocation
                if(abs(currentLocation.latitude - previousLocation.latitude) > EPS || abs(currentLocation.longitude - previousLocation.longitude) > EPS){
                    val distance: Float = currentLocation.distanceTo(previousLocation)
                    totalDistance += distance
                }

                updateUIValues(currentLocation)
            }
        }

        locationRequest = LocationRequest()
        locationRequest.interval = 1000 * 30
        locationRequest.fastestInterval = 1000 * 5

        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        updateGPS()
        startLocationUpdates()

//        timer.scheduleAtFixedRate(object : TimerTask(){
//            override fun run() {
//                elapsedTimeSeconds++
//                tv_time.text = elapsedTimeSeconds.toString()
//            }
//
//        }, 1000, 1000)

        startTime = System.currentTimeMillis()

        handler = Handler()
        runnable = object : Runnable{
            override fun run() {
                val millis: Long = System.currentTimeMillis() - startTime
                val seconds: Int = ((millis / 1000) % 60).toInt()

                tv_time.text = seconds.toString()
                handler.postDelayed(this, 500)
            }
        }

        handler.postDelayed(runnable, 0)

        return view
    }

    private fun updateGPS() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(activity)

        if(ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProvider.lastLocation.addOnSuccessListener(activity!!, object : OnSuccessListener<Location> {
                override fun onSuccess(p0: Location?) {
                    previousLocation = Location(p0)
                    updateUIValues(p0!!)
                }

            })
        }else{
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_FINE_LOCATION)
        }
    }

    private fun updateUIValues(location: Location) {
        tv_latitude.text = String.format("%.2f", location.latitude)
        tv_longitude.text = String.format("%.2f", location.longitude)
        tv_distance.text = String.format("%.2f", totalDistance)
        tv_speed.text = String.format("%.2f", location.speed)
    }

    private fun startLocationUpdates(){

        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_FINE_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                updateGPS()
            }else{
                Toast.makeText(activity, "App doesn't have permission to use location", Toast.LENGTH_SHORT).show()
                activity!!.finish()
            }
        }
    }
}