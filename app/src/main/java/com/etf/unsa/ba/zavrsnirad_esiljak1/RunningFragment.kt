package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ba.zavrsnirad_esiljak1.R
import com.google.android.gms.location.*


class RunningFragment : Fragment() {

    private val PERMISSION_FINE_LOCATION: Int = 99
    private var totalDistance: Double = 0.0
    private var elapsedTime: Long = 0
    private var isStopped = true
    private var isLocked = false

    private lateinit var tv_time: TextView
    private lateinit var tv_speed: TextView
    private lateinit var tv_distance: TextView
    private lateinit var start_btn: ImageButton
    private lateinit var pause_btn: ImageButton
    private lateinit var stop_btn: ImageButton
    private lateinit var lock_btn: ImageButton

    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var previousLocation: Location
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private val startRunListener = View.OnClickListener {
        if(isLocked) {
            Toast.makeText(activity, "Locked", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        start_btn.visibility = View.GONE
        pause_btn.visibility = View.VISIBLE
        stop_btn.visibility = View.VISIBLE
        isStopped = false

        handler.postDelayed(runnable, 0)
    }
    private val pauseRunListener = View.OnClickListener {
        if(isLocked) {
            Toast.makeText(activity, "Locked", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        pause_btn.visibility = View.GONE
        start_btn.visibility = View.VISIBLE
        isStopped = true

        handler.removeCallbacks(runnable)
    }

    private val lockScreenListener = View.OnClickListener {
        if(isLocked)
            unlockDialog()
        else
            lockDialog()

        (activity as MainActivity).delayedHide(100)
    }

    private val stopRunListener = View.OnClickListener {
        if(isLocked) {
            Toast.makeText(activity, "Locked", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        val builder = AlertDialog.Builder(context)

        builder.setTitle("Ending workout")
            .setMessage("Are you sure that you want to end your workout")
            .setPositiveButton("Yes") { dialog, which ->
                val runDetailFragment = RunDetailFragment()

                activity!!.supportFragmentManager.beginTransaction().replace(R.id.running_view, runDetailFragment).commit()
            }
            .setNegativeButton("No") {dialog, which ->
            }
            .show()

        (activity as MainActivity).delayedHide(100)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_running, container, false)

        tv_time = view.findViewById(R.id.tv_time)
        tv_speed = view.findViewById(R.id.tv_speed)
        tv_distance = view.findViewById(R.id.tv_distance)
        start_btn = view.findViewById(R.id.start_btn)
        pause_btn = view.findViewById(R.id.pause_btn)
        stop_btn = view.findViewById(R.id.stop_btn)
        lock_btn = view.findViewById(R.id.lock_btn)

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                val currentLocation = p0.lastLocation
                val distance: Float = currentLocation.distanceTo(previousLocation)
                if(distance >= 10){
                    totalDistance += distance
                    previousLocation = currentLocation
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

        handler = Handler()
        runnable = object : Runnable{
            override fun run() {
                val seconds = (elapsedTime % 60).toInt()
                var minutes = (elapsedTime / 60).toInt()
                val hours = minutes / 60
                minutes %= 60

                if(hours != 0){
                    tv_time.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                }else{
                    tv_time.text = String.format("%02d:%02d", minutes, seconds)
                }

                elapsedTime++
                handler.postDelayed(this, 1000)
            }
        }

        start_btn.setOnClickListener(startRunListener)
        pause_btn.setOnClickListener(pauseRunListener)
        stop_btn.setOnClickListener(stopRunListener)
        lock_btn.setOnClickListener(lockScreenListener)

        return view
    }

    private fun updateGPS() {
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(activity)

        if(ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProvider.lastLocation.addOnSuccessListener(activity!!
            ) { p0 ->
                previousLocation = Location(p0)
                updateUIValues(p0!!)
            }
        }else{
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_FINE_LOCATION)
        }
    }

    private fun updateUIValues(location: Location) {
        if(isStopped) return
        tv_distance.text = String.format("%.2f", totalDistance/1000)
        tv_speed.text = String.format("%.2f", location.speed)
    }

    private fun startLocationUpdates(){

        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun lockDialog(){
        var builder = AlertDialog.Builder(context)

        builder.setTitle("Locking screen")
                .setMessage("Are you sure that you want to lock your screen")
                .setPositiveButton("Yes") { dialog, which ->
                                                    isLocked = true
                                                    lock_btn.setBackgroundResource(R.drawable.ic_baseline_lock_24)
                                                }
                .setNegativeButton("No") {dialog, which ->
                                                    isLocked = false
                                                }
                .show()
    }

    private fun unlockDialog(){
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Unlocking screen")
                .setMessage("Are you sure that you want to unlock your screen")
                .setPositiveButton("Yes") { dialog, which ->
                                                    isLocked = false
                                                    lock_btn.setBackgroundResource(R.drawable.ic_baseline_lock_open_24)
                                                }
                .setNegativeButton("No") {dialog, which ->
                                                    isLocked = true
                                                }
                .show()
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