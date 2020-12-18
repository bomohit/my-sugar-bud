package com.bit.mysugarbud.ui.slideshow

import android.graphics.Color
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bit.mysugarbud.R
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CentreLocation(private val locations: MutableList<Locations>) :
    RecyclerView.Adapter<CentreLocation.ViewHolder>() {
    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val Name: TextView = itemView.findViewById(R.id.centre_name)
        val location: TextView = itemView.findViewById(R.id.centre_location)
        val time: TextView = itemView.findViewById(R.id.centre_time)
        val status: TextView = itemView.findViewById(R.id.centre_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.health_centre_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Locations = locations[position]
        holder.Name.text = Locations.name
        holder.location.text = Locations.location
        holder.time.text = Locations.open

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("h a")
        val cTime = current.format(formatter)
        val currentT = LocalTime.parse(cTime, formatter)
        val open = LocalTime.parse(Locations.open, formatter)
        val close = LocalTime.parse(Locations.close, formatter)

        d("bomoh", "currentTime: $currentT , $open")

        if (currentT.isAfter(open) && currentT.isBefore(close)) {
            d("bomoh", "status: open")
            holder.status.text = "OPEN"
            holder.status.setTextColor(Color.BLUE)
            holder.time.text = "Closes ${Locations.close}"
        } else {
            d("bomoh", "status: close")
            holder.status.text = "CLOSE"
            holder.status.setTextColor(Color.RED)
            holder.time.text = "Opens ${Locations.open}"

        }

    }

    override fun getItemCount() = locations.size

}
