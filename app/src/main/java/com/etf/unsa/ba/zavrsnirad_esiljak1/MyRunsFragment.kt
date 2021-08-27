package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ba.zavrsnirad_esiljak1.R
import com.google.firebase.database.DataSnapshot

class MyRunsFragment : Fragment(), DatabaseInterface {

    private var runList = ArrayList<Run>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var lifetimeDistance: TextView
    private lateinit var numberOfRuns: TextView
    private lateinit var lifetimeTime: TextView
    private lateinit var profile: TextView
    private lateinit var loadingView: ConstraintLayout

    private val onClickListener =  View.OnClickListener{
        requireActivity().onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_runs, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        backButton = view.findViewById(R.id.back_btn_my_runs)
        lifetimeDistance = view.findViewById(R.id.lifetimeDistance_tw)
        numberOfRuns = view.findViewById(R.id.numberOfRuns_tw)
        lifetimeTime = view.findViewById(R.id.lifetimeTime_tw)
        profile = view.findViewById(R.id.profile_tw)
        loadingView = view.findViewById(R.id.loadingView)

        loadingView.visibility = View.VISIBLE
        FirebaseDBInteractor.instance.getMyRuns(getCurrentUser().uuid!!, this)
        profile.text = getInitials()

        backButton.setOnClickListener(onClickListener)

        return view
    }

    private fun getInitials(): String{
        val tempList = getCurrentUser().userName.split(" ")
        var ret = ""
        for(s in tempList){
            ret += s.toUpperCase()[0]
        }
        return ret
    }

    private fun setAdapter(){
        if(context == null)
            return
        recyclerView.adapter = ItemAdapter(requireContext(), runList) { run ->
            val fragment = RunDetailFragment()
            val fm = requireActivity().supportFragmentManager

            val bundle = Bundle()
            bundle.putBoolean("post", false)
            bundle.putParcelable("run", run)

            fragment.arguments = bundle
            fm.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left).replace(R.id.view, fragment, "detail").addToBackStack(null).commit()
        }
        setupUIParameters()
    }

    private fun getCurrentUser(): User{
        return (requireActivity() as MainActivity).user!!
    }

    private fun formatTime(time: Long) {
        val seconds = (time % 60).toInt()
        var minutes = (time / 60).toInt()
        val hours = minutes / 60
        minutes %= 60

        if(hours != 0){
            lifetimeTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }else{
            lifetimeTime.text = String.format("%02d:%02d", minutes, seconds)
        }
    }

    private fun setupUIParameters(){
        numberOfRuns.text = runList.size.toString()
        var dist = 0f
        runList.forEach{ run -> dist += run.distanceMeters }
        lifetimeDistance.text = String.format("%.2f", dist / 1000.0)

        var seconds = 0L
        runList.forEach{ run -> seconds += run.durationSeconds }
        formatTime(seconds)
        loadingView.visibility = View.GONE
    }

    override fun onSuccess(snapshot: DataSnapshot?) {
        runList.clear()
        if(snapshot == null) {
            Toast.makeText(requireActivity(), R.string.no_runs, Toast.LENGTH_SHORT).show()
            return
        }
        for(item in snapshot.children){
            runList.add(item.getValue(Run::class.java)!!)
        }

        runList.reverse()
        setAdapter()
    }

    override fun onFailure() {
        Toast.makeText(requireActivity(), R.string.failed_runs, Toast.LENGTH_SHORT).show()
    }
}