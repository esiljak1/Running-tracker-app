package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ba.zavrsnirad_esiljak1.R
import com.google.firebase.database.DataSnapshot

class MyRunsFragment : Fragment(), DatabaseInterface {

    private var runList = ArrayList<Run>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var backButton: ImageButton

    private val onClickListener =  View.OnClickListener{
        requireActivity().onBackPressed()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_runs, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        backButton = view.findViewById(R.id.back_btn_my_runs)
        FirebaseDBInteractor.instance.getMyRuns(getCurrentUser().uuid!!, this)

        backButton.setOnClickListener(onClickListener)

        return view
    }

    private fun setAdapter(){
        if(context == null)
            return
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

    override fun onSuccess(snapshot: DataSnapshot?) {
        runList.clear()
        if(snapshot == null) {
            Toast.makeText(requireActivity(), "No runs found for this account", Toast.LENGTH_SHORT).show()
            return
        }
        for(item in snapshot.children){
            runList.add(item.getValue(Run::class.java)!!)
        }

        runList.reverse()
        setAdapter()
    }

    override fun onFailure() {
        Toast.makeText(requireActivity(), "Failed to load myRuns", Toast.LENGTH_SHORT).show()
    }
}