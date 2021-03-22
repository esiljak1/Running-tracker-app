package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ba.zavrsnirad_esiljak1.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

//TODO Add new Interface, MapFragment and RunningFragment implement that interface. Both implement one method updateUIValues
//TODO Add new Class and move all of the location tracking to that class

class MapFragment : Fragment() {

    private val PERMISSION_FINE_LOCATION: Int = 99
    private val ZOOM_LEVEL: Float = 19.5f

    private lateinit var mMap: GoogleMap
    private val onClick = View.OnClickListener{
        val fragment = RunningFragment()
        val fm = activity!!.supportFragmentManager

        fm.beginTransaction().replace(R.id.view, fragment, "running").commit()
    }
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
    }

    private lateinit var floatingButton: FloatingActionButton
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private val locationRequest = LocationRequest()
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            Toast.makeText(activity, "Updated location", Toast.LENGTH_SHORT).show()

            updateUIValues(p0.lastLocation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            PackageManager.PERMISSION_GRANTED
        )
        val view =  inflater.inflate(R.layout.fragment_map, container, false)
        floatingButton = view.findViewById(R.id.btn_run)

        floatingButton.scaleType = ImageView.ScaleType.FIT_CENTER

        floatingButton.setOnClickListener(onClick)

        locationRequest.interval = 1000 * 30
        locationRequest.fastestInterval = 1000 * 5

        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY

        updateGPS()
        startLocationUpdates()

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    private fun updateGPS() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(activity)

        if(ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProvider.lastLocation.addOnSuccessListener(
                activity!!
            ) { p0 ->
                updateUIValues(p0!!)
            }
        }else{
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_FINE_LOCATION
            )
        }
    }

    private fun startLocationUpdates(){

        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun updateUIValues(p0: Location) {
        mMap.clear()
        val currentLocation = LatLng(p0.latitude, p0.longitude)

        mMap.addMarker(MarkerOptions().position(currentLocation))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_LEVEL))
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