package com.bit.mysugarbud.ui.reminder

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bit.mysugarbud.R

class ReminderAdapter(private val reminder: MutableList<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {
    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout : ConstraintLayout = itemView.findViewById(R.id.reminderRowLayout)
        val title: TextView = itemView.findViewById(R.id.reminderTitle)
        val time: TextView = itemView.findViewById(R.id.reminderTime)
        val day: TextView = itemView.findViewById(R.id.reminderDay)
        val switch: TextView = itemView.findViewById(R.id.reminderSwitch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reminder_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val remain = reminder[position]
        holder.title.text = remain.title
        holder.time.text = remain.time
        holder.day.text = remain.day

        holder.layout.setOnLongClickListener {
            d("bomoh", "long click detected")

            reminder.removeAt(position)
            notifyDataSetChanged()
            true
        }

    }

    override fun getItemCount() = reminder.size


}
