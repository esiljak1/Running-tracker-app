package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ba.zavrsnirad_esiljak1.R

class MyRunsFragment : Fragment(), DatabaseInterface {

    private var runList = ArrayList<Run>()

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_runs, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        FirebaseDBInteractor.instance.getMyRuns(getCurrentUser().uuid!!, this)

        return view
    }

    private fun setAdapter(){
        recyclerView.adapter = ItemAdapter(requireContext(), runList) { run ->
            val fragment = RunDetailFragment()
            val fm = requireActivity().supportFragmentManager

            val bundle = Bundle()
            bundle.putParcelable("run", run)

            fragment.arguments = bundle
            fm.beginTransaction().replace(R.id.view, fragment, "myRuns").addToBackStack(null).commit()
        }
    }

    private fun getCurrentUser(): User{
        return (requireActivity() as MainActivity).user!!
    }

    override fun onSuccess(o: Any?) {
        runList = ArrayList(o as List<Run>)
        setAdapter()
    }

    override fun onFailure() {
        Toast.makeText(requireActivity(), "Failed to load myRuns", Toast.LENGTH_SHORT).show()
    }
}