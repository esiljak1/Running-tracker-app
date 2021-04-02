package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ba.zavrsnirad_esiljak1.R

class MyRunsFragment : Fragment() {

    private val runList = ArrayList<Run>()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_runs, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        fillRunList()

        recyclerView.adapter = ItemAdapter(requireContext(), runList)

        return view
    }

    private fun fillRunList(){
        runList.clear()
        runList.add(Run(1, 1000f, 10f, 2000, ArrayList()))
        runList.add(Run(1, 1000f, 10f, 2000, ArrayList()))
        runList.add(Run(1, 1000f, 10f, 2000, ArrayList()))
        runList.add(Run(1, 1000f, 10f, 2000, ArrayList()))
        runList.add(Run(1, 1000f, 10f, 2000, ArrayList()))
        runList.add(Run(1, 1000f, 10f, 2000, ArrayList()))
    }
}