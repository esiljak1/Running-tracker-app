package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ba.zavrsnirad_esiljak1.R

class ItemAdapter (private val context: Context, private val dataset: List<Run>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.tv_run_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.run_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        val date = item.endOfTheRun
        holder.textView.text = date!!.toLocalDate().toString()
    }

    override fun getItemCount() = dataset.size

}