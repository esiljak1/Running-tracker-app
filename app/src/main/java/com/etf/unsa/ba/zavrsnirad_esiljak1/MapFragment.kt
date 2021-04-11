package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ba.zavrsnirad_esiljak1.R
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MapFragment : Fragment(), MapUIInterface {

    private val PERMISSION_FINE_LOCATION: Int = 99
    private val ZOOM_LEVEL: Float = 19.5f
    private var isInitiated = false

    private lateinit var mMap: GoogleMap
    private val newRunFragmentOnClick = View.OnClickListener{
        val fragment = RunningFragment()
        val fm = requireActivity().supportFragmentManager

        fm.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left).replace(R.id.view, fragment, "running").addToBackStack(null).commit()
    }
    private val myRunsFragmentOnClick = View.OnClickListener {
        val fragment = MyRunsFragment()
        val fm = requireActivity().supportFragmentManager

        fm.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left).replace(R.id.view, fragment, "myRuns").addToBackStack(null).commit()
    }
    private val aboutFragmentOnClick = View.OnClickListener {
        Toast.makeText(requireActivity(), "Srry Nejra ovo još ne radi", Toast.LENGTH_LONG).show()
    }
    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
    }

    private lateinit var myRunsButton: AppCompatButton
    private lateinit var runButton: FloatingActionButton
    private lateinit var aboutButton: ImageButton
    private val locationHandler = HandlerLocation.instance

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_map, container, false)

        if(!(requireActivity() as MainActivity).isInitiated) {
            Toast.makeText(
                requireActivity(),
                "Welcome " + getCurrentUser().userName,
                Toast.LENGTH_SHORT
            ).show()
            (requireActivity() as MainActivity).isInitiated = true
        }

        runButton = view.findViewById(R.id.btn_run)
        aboutButton = view.findViewById(R.id.settings_btn)
        myRunsButton = view.findViewById(R.id.my_runs_btn)

        runButton.scaleType = ImageView.ScaleType.FIT_CENTER
        runButton.setOnClickListener(newRunFragmentOnClick)

        myRunsButton.setOnClickListener(myRunsFragmentOnClick)
        aboutButton.setOnClickListener(aboutFragmentOnClick)

        locationHandler.ui = this
        locationHandler.start()

        (requireActivity() as MainActivity).delayedHide(100)

        return view
    }

    private fun getCurrentUser() : User{
        return (requireActivity() as MainActivity).user!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    override fun updateUIValues(location: Location) {
        mMap.clear()
        val currentLocation = LatLng(location.latitude, location.longitude)

        mMap.addMarker(MarkerOptions().position(currentLocation))

        if(!isInitiated) {
            isInitiated = true
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, ZOOM_LEVEL))
        }
    }

    override fun postLocationCallback(locationResult: LocationResult) {
        updateUIValues(locationResult.lastLocation)
    }

    override fun get(): FragmentActivity?{
        return activity
    }

    override fun permissions() {
        requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_FINE_LOCATION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationHandler.updateGPS()
            }else{
                Toast.makeText(activity, "App doesn't have permission to use location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}