package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ba.zavrsnirad_esiljak1.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FirstFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private val onClick = View.OnClickListener{
        val fragment = RunningFragment()
        val fm = activity!!.supportFragmentManager

        fm.beginTransaction().replace(R.id.view, fragment, "running").commit()
    }
    private val callback = OnMapReadyCallback { googleMap ->
        println("Ovdje")
        mMap = googleMap
    }
    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager
    private lateinit var button: Button

    private val MIN_TIME: Long = 1000
    private val MIN_DIST: Float = 5f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PackageManager.PERMISSION_GRANTED)
        val view =  inflater.inflate(R.layout.fragment_first, container, false)
        button = view.findViewById(R.id.btn_run)

        button.setOnClickListener(onClick)

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        locationListener = object: LocationListener{
            override fun onLocationChanged(location: Location) {
                println("Listener")
                val latLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(latLng).title("Marker in Sydney"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }

        }

        locationManager = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager

        println("Manager: " + locationManager)

        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            println("Nemam prava")
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener)
    }
}