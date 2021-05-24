package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ba.zavrsnirad_esiljak1.R
import com.google.android.gms.location.*


class RunningFragment : Fragment(), MapUIInterface {

    private val speedSamplesList = ArrayList<Float>()

    private val PERMISSION_FINE_LOCATION: Int = 99
    private val TO_KMPH = 3.6
    private var elapsedTime: Long = 0

    private var totalDistance: Float = 0f
    private var topSpeed: Float = 0f
    private var currentSpeed: Float = 0f

    private var isStopped = true
    private var isLocked = false

    private lateinit var tv_time: TextView
    private lateinit var tv_speed: TextView
    private lateinit var tv_distance: TextView
    private lateinit var start_btn: ImageButton
    private lateinit var pause_btn: ImageButton
    private lateinit var stop_btn: ImageButton
    private lateinit var lock_btn: ImageButton

    private lateinit var previousLocation: Location
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val handlerLocation = HandlerLocation.instance

    private val handlerSpeedSamples: Handler = Handler(Looper.getMainLooper())
    private lateinit var runnableSpeedSamples: Runnable

    private val startRunListener = View.OnClickListener {
        if(isLocked) {
            Toast.makeText(requireActivity(), "Locked", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        start_btn.visibility = View.GONE
        pause_btn.visibility = View.VISIBLE
        stop_btn.visibility = View.VISIBLE
        isStopped = false

        handler.postDelayed(runnable, 0)
        handlerSpeedSamples.postDelayed(runnableSpeedSamples, 0)
    }
    private val pauseRunListener = View.OnClickListener {
        if(isLocked) {
            Toast.makeText(requireActivity(), "Locked", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        pause_btn.visibility = View.GONE
        start_btn.visibility = View.VISIBLE
        isStopped = true

        handler.removeCallbacks(runnable)
        handlerSpeedSamples.removeCallbacks(runnableSpeedSamples)
    }

    private val lockScreenListener = View.OnClickListener {
        if(isLocked)
            unlockDialog()
        else
            lockDialog()

        (requireActivity() as MainActivity).delayedHide(100)
    }

    private val stopRunListener = View.OnClickListener {
        if(isLocked) {
            Toast.makeText(requireActivity(), "Locked", Toast.LENGTH_SHORT).show()
            return@OnClickListener
        }

        val builder = AlertDialog.Builder(context)

        builder.setTitle("Ending workout")
            .setMessage("Are you sure that you want to end your workout")
            .setPositiveButton("Yes") { dialog, which ->
                isStopped = true
                val runDetailFragment = RunDetailFragment()
                val bundle = Bundle()

                val thisRun = Run(totalDistance, topSpeed, elapsedTime, speedSamplesList)

                FirebaseDBInteractor.instance.postRun(getCurrentUser().uuid!!, thisRun)

                bundle.putBoolean("post", true)
                bundle.putParcelable("run", thisRun)
                runDetailFragment.arguments = bundle

                handlerLocation.stop()

                val fm = requireActivity().supportFragmentManager

                fm.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left).replace(R.id.running_view, runDetailFragment).commit()
            }
            .setNegativeButton("No") {dialog, which ->
            }
            .show()

        (requireActivity() as MainActivity).delayedHide(100)
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

        handler = Handler(Looper.getMainLooper())
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

        runnableSpeedSamples = object : Runnable{
            override fun run() {
                speedSamplesList.add(currentSpeed)

                handlerSpeedSamples.postDelayed(this, 10 * 1000)
            }

        }

        start_btn.setOnClickListener(startRunListener)
        pause_btn.setOnClickListener(pauseRunListener)
        stop_btn.setOnClickListener(stopRunListener)
        lock_btn.setOnClickListener(lockScreenListener)

        handlerLocation.ui = this
        handlerLocation.start()

        return view
    }

    private fun getCurrentUser(): User{
        return (requireActivity() as MainActivity).user!!
    }

    private fun lockDialog(){
        val builder = AlertDialog.Builder(context)

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

    override fun updateUIValues(location: Location) {
        if(isStopped) return

        tv_distance.text = String.format("%.2f", totalDistance/1000)
        currentSpeed = (location.speed * TO_KMPH).toFloat()
        if(currentSpeed > topSpeed)
            topSpeed = currentSpeed
        tv_speed.text = String.format("%.2f", location.speed)
    }

    override fun postLocationCallback(locationResult: LocationResult) {
        if(!this::previousLocation.isInitialized){
            previousLocation = Location(locationResult.lastLocation)
        }

        val currentLocation = locationResult.lastLocation
        val distance: Float = currentLocation.distanceTo(previousLocation)
        if(distance >= 10){
            totalDistance += distance
            previousLocation = Location(currentLocation)
        }
        updateUIValues(currentLocation)
    }

    override fun get(): FragmentActivity? {
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
                handlerLocation.updateGPS()
            }else{
                Toast.makeText(requireActivity(), "App doesn't have permission to use location", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }
    }
}