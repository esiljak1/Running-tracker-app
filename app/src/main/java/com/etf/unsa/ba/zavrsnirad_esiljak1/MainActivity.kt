package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ba.zavrsnirad_esiljak1.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity() {

    private val PERMISSION_FINE_LOCATION: Int = 99
    private val EPS: Double = 0.001
    private var totalDistance: Double = 0.0

    private lateinit var tv_lat: TextView
    private lateinit var tv_long: TextView
    private lateinit var tv_distance: TextView

    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var previousLocation: Location

    private var startup: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fm = supportFragmentManager

        startup = fm.findFragmentByTag("startup")

        if(startup == null){
            startup = FirstFragment()
            fm.beginTransaction().replace(R.id.view, startup as FirstFragment, "startup").commit()
        }else{
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

//        tv_lat = findViewById(R.id.tv_lat)
//        tv_long = findViewById(R.id.tv_long)
//        tv_distance = findViewById(R.id.tv_distance)
//
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(p0: LocationResult) {
//                super.onLocationResult(p0)
//
//                val currentLocation = p0.lastLocation
//
//                if(abs(currentLocation.latitude - previousLocation.latitude) > EPS || abs(currentLocation.longitude - previousLocation.longitude) > EPS){
//                    val distance: Float = currentLocation.distanceTo(previousLocation)
//                    totalDistance += distance
//                }
//
//                updateUIValues(currentLocation)
//            }
//        }
//
//        locationRequest = LocationRequest()
//        locationRequest.interval = 1000 * 30
//        locationRequest.fastestInterval = 1000 * 5
//
//        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//
//        updateGPS()
//        startLocationUpdates()
    }

    private fun updateGPS(){
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProvider.lastLocation.addOnSuccessListener(this, object : OnSuccessListener<Location>{
                override fun onSuccess(p0: Location?) {
                    previousLocation = Location(p0)
                    updateUIValues(p0)
                }

            })
        }else{
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_FINE_LOCATION)
        }
    }

    private fun updateUIValues(location: Location?) {
        if(location != null){
            tv_lat.text = location.latitude.toString()
            tv_long.text = location.longitude.toString()
            tv_distance.text = totalDistance.toString()
        }
    }

    private fun startLocationUpdates(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(this, "App doesn't have permission to use location", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}