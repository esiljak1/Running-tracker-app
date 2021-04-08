package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ba.zavrsnirad_esiljak1.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class RunDetailFragment : Fragment() {

    private lateinit var run: Run
    private var entries: ArrayList<Entry> = ArrayList()

    private lateinit var tv_date: TextView
    private lateinit var tv_recorded_distance: TextView
    private lateinit var tv_recorded_duration: TextView
    private lateinit var tv_recorded_speed: TextView
    private lateinit var back_btn: ImageButton
    private lateinit var chart_speed_samples: LineChart

    private val backListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            activity!!.onBackPressed()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_run_detail, container, false)

        tv_date = view.findViewById(R.id.tv_run_name)
        tv_recorded_distance = view.findViewById(R.id.tv_recorded_distance)
        tv_recorded_duration = view.findViewById(R.id.tv_recorded_duration)
        tv_recorded_speed = view.findViewById(R.id.tv_recorded_speed)
        back_btn = view.findViewById(R.id.back_btn)
        chart_speed_samples = view.findViewById(R.id.chart_speed_samples)

        if(requireArguments().get("run") != null){
            run = requireArguments().get("run") as Run
            setUI()
        }

        if(requireArguments().get("post") != null){
            if(requireArguments().get("post") as Boolean && !(requireActivity() as MainActivity).isNetworkAvailable()){
                openDialog()
            }
        }

        back_btn.setOnClickListener(backListener)

        return view
    }

    private fun setUI(){
        tv_date.text = run.endOfTheRun.toString()
        tv_recorded_distance.text = String.format("%.2f", run.distanceMeters / 1000)
        tv_recorded_duration.text = formatTime(run.durationSeconds)
        tv_recorded_speed.text = String.format("%.2f", run.topSpeed)
        drawChart()
    }

    private fun formatTime(time: Long) : String{
        val seconds = (time % 60).toInt()
        var minutes = (time / 60).toInt()
        val hours = minutes / 60
        minutes %= 60

        if(hours != 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)

        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun drawChart(){
        entries = ArrayList()
        entries.clear()

        var i = 0
        for (entry in run.speedSamples!!){
            entries.add(Entry(i.toFloat(), entry))
            i += 10
        }

        val lineDataSet =  LineDataSet(entries, "Data Set 1")
        lineDataSet.color = Color.YELLOW
        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(lineDataSet)

        val data = LineData(dataSets)
        chart_speed_samples.data = data
        chart_speed_samples.xAxis.textColor = Color.WHITE
        chart_speed_samples.axisLeft.textColor = Color.WHITE
        chart_speed_samples.axisRight.textColor = Color.WHITE
    }

    private fun openDialog(){
        val builder = AlertDialog.Builder(context)

        builder.setTitle("You're not connected to the internet")
                .setMessage("Please connect to the internet in order to save your run")
                .setPositiveButton("Close") { dialog, which ->
                }
                .show()
    }

}