package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ba.zavrsnirad_esiljak1.R

class ItemAdapter (private val context: Context, private val dataset: List<Run>,
                   private val listener: (Run) -> Unit) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        val tvDate: TextView = view.findViewById(R.id.tv_run_date)
        val distanceItem: TextView = view.findViewById(R.id.item_distance)
        val durationItem: TextView = view.findViewById(R.id.item_duration)

        fun bind(run: Run){
            val date = run.endOfTheRun
            tvDate.text = date
            distanceItem.text = String.format("%.2f", run.distanceMeters/1000) + " km"
            durationItem.text = formatTime(run.durationSeconds)
        }

        private fun formatTime(total: Long): String{
            val seconds = (total % 60).toInt()
            var minutes = (total / 60).toInt()
            val hours = minutes / 60
            minutes %= 60

            if(hours > 0)
                return String.format("%02d:%02d:%02d", hours, minutes, seconds)

            return String.format("%02d:%02d", minutes, seconds)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.run_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(dataset[position])
        holder.itemView.setOnClickListener{
            listener(dataset[position])
        }
    }

    override fun getItemCount() = dataset.size

}