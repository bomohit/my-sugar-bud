package com.bit.mysugarbud.ui.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bit.mysugarbud.MainActivity
import com.bit.mysugarbud.R
import java.util.*

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
            d("bomoh", "long click detected : profile: ${remain.profile}")

            reminder.removeAt(position)

            val sharedPreferences = holder.itemView.context.getSharedPreferences(remain.profile, Context.MODE_PRIVATE)
            // Delete SharePreferences Profile
            sharedPreferences.edit().clear().apply()
            // Delete Alarm
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            cal.set(Calendar.HOUR_OF_DAY, 13)

            val alarmManager = holder.itemView.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(holder.itemView.context, Receiver::class.java)
            val usedT = remain.profile.split(" ")
            val pendingIntent = PendingIntent.getBroadcast(holder.itemView.context, usedT[2].toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
            d("bomoh", "AlarmRemove: ${usedT[2]}")

            // notify the recycler view to refresh/reload
            notifyDataSetChanged()
            true
        }

    }

    override fun getItemCount() = reminder.size


}
