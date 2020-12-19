package com.bit.mysugarbud.ui.reminder

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bit.mysugarbud.MainActivity
import com.bit.mysugarbud.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class ReminderFragment : Fragment() {
    var day = mutableListOf<String?>()
    val reminder = mutableListOf<Reminder>()
    var H = 0
    var M = 0
    var t = 0
    var usedT = 0
    var endT = 0
    var slot = mutableListOf<Int>()

    private val CHANNEL_ID = "channel_id_mysugarbud"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_reminder, container, false)
        val uid = (activity as MainActivity).uid

        for ( i in 1..5) {

            val sharedPreferences = requireActivity().getSharedPreferences("$uid Alarm $i", Context.MODE_PRIVATE)
            val tit = sharedPreferences.getString("Alarm $i Title", "")
            if (!tit.isNullOrEmpty()) {
                val time = sharedPreferences.getString("Alarm $i Time", "").toString()
                val day = sharedPreferences.getString("Alarm $i Day", "").toString()
                val end = sharedPreferences.getString("Alarm $i End", "").toString()
                t = end.toInt() + 1
                usedT = t
                val profile = "$uid Alarm $i"
                reminder.add(Reminder(profile, tit, time, day))

                d("bomoh", "sharedPreferences: time:$time, day:$day, end:$end, t:$t, usedT:$usedT")
            } else {
                slot.add(i)
                d("bomoh", "sharedPreferences: EMPTY : $i : slot: $slot : size: ${slot.size}")
                // USE FOR DELETING SHAREDPREFERENCE
//                val editor = sharedPreferences.edit().clear().commit()
            }


        }

        rv(root)

        return root
    }

    private fun rv(root: View) {
        val recyclerView = root.findViewById<RecyclerView>(R.id.reminderRecyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ReminderFragment.context)
            adapter = ReminderAdapter(reminder)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)

        view.findViewById<Button>(R.id.buttonAddReminder).setOnClickListener {
            if (slot.size == 0) {
                Snackbar.make(requireView(), "Failed : Alarm full ", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val reminderView = layoutInflater.inflate(R.layout.dialog_add_item, null)
            builder.setView(reminderView)
                    .setPositiveButton("Confirm", DialogInterface.OnClickListener { dialog, id ->
                        var selDay :String? = ""
                        val dialogView = dialog as Dialog
                        val title = dialogView.findViewById<EditText>(R.id.editTitle).text.toString()
                        val time = dialogView.findViewById<TextView>(R.id.displayTime).text.toString()

                            if (title.isNullOrEmpty() or time.isNullOrEmpty() or day.isNullOrEmpty()) {
                                Snackbar.make(requireView(), "Failed to Create", Snackbar.LENGTH_SHORT).show()
                            } else {
                                Snackbar.make(requireView(), "Reminder Added", Snackbar.LENGTH_SHORT).show()
                                d("bomoh", "day:$day")
                                for (i in 0..6) {
                                    if (i == 0 && day.contains("Monday")) {
                                        selDay += "Mon"
                                    } else if (i == 1 && day.contains("Tuesday")) {
                                        if (selDay!!.isNotEmpty()) {
                                            selDay += ", "
                                        }
                                        selDay += "Tue"
                                    } else if (i == 2 && day.contains("Wednesday")) {
                                        if (selDay!!.isNotEmpty()) {
                                            selDay += ", "
                                        }
                                        selDay += "Wed"
                                    } else if (i == 3 && day.contains("Thursday")) {
                                        if (selDay!!.isNotEmpty()) {
                                            selDay += ", "
                                        }
                                        selDay += "Thu"
                                    } else if (i == 4 && day.contains("Friday")) {
                                        if (selDay!!.isNotEmpty()) {
                                            selDay += ", "
                                        }
                                        selDay += "Fri"
                                    } else if (i == 5 && day.contains("Saturday")) {
                                        if (selDay!!.isNotEmpty()) {
                                            selDay += ", "
                                        }
                                        selDay += "Sat"
                                    } else if (i == 6 && day.contains("Sunday")) {
                                        if (selDay!!.isNotEmpty()) {
                                            selDay += ", "
                                        }
                                        selDay += "Sun"
                                    }
                                }
                                val uid = (activity as MainActivity).uid

                                // SAVE THE ALARM TITLE AND TIME AND DAY AND T IN MEMORY
                                val sharedPreferences = requireActivity().getSharedPreferences("$uid Alarm ${slot[0]}", Context.MODE_PRIVATE)
                                val editor  = sharedPreferences.edit()
                                editor.putString("Alarm ${slot[0]} Title", title)
                                editor.putString("Alarm ${slot[0]} Time", time)
                                editor.putString("Alarm ${slot[0]} Day", selDay)
                                editor.putString("Alarm ${slot[0]} Start", t.toString())
                                editor.apply()
                                // continue -> createAlarm()
                                val profile = "$uid Alarm ${slot[0]}"
                                reminder.add(Reminder(profile, title, time, selDay.toString()))
                                rv(requireView())
                                createAlarm(title)
                            }
                            d("bomoh", "uid: $title, day:$day, selDay:$selDay")


                        return@OnClickListener

                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->
                        H = 0
                        M = 0
                        day.clear()
                        dialog.cancel()
                        return@OnClickListener
                    })

            reminderAct(reminderView)

            builder.create()
            builder.show()


        }
    }

    private fun createAlarm(title: String) {
        val cal = Calendar.getInstance()
        val prof = (activity as MainActivity).uid
        val sharedPreferences = requireActivity().getSharedPreferences("$prof Alarm ${slot[0]}", Context.MODE_PRIVATE)
        for (i in day) {
            if (i == "Monday") {
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                cal.set(Calendar.HOUR_OF_DAY, H)
                cal.set(Calendar.MINUTE, M)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                // REQUEST CODE DIFFERENTIATE EACH
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, Receiver::class.java)
                intent.putExtra("Title", "$title")
                val pendingIntent = PendingIntent.getBroadcast(context, usedT, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent)
                usedT += 1
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)

            }
            else if ( i == "Tuesday") {
                cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
                cal.set(Calendar.HOUR_OF_DAY, H)
                cal.set(Calendar.MINUTE, M)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                if (cal.timeInMillis < System.currentTimeMillis() || cal.before(GregorianCalendar())) {
                    cal.set(Calendar.HOUR_OF_DAY, H +24)
                    cal.add(GregorianCalendar.DAY_OF_MONTH, 7)
                }

                // REQUEST CODE DIFFERENTIATE EACH
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, Receiver::class.java)
                intent.putExtra("Title", "$title")
                val pendingIntent = PendingIntent.getBroadcast(context, usedT, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                usedT += 1
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
            } else if ( i == "Wednesday") {
                cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
                cal.set(Calendar.HOUR_OF_DAY, H)
                cal.set(Calendar.MINUTE, M)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                if (cal.timeInMillis < System.currentTimeMillis() || cal.before(GregorianCalendar())) {
                    cal.set(Calendar.HOUR_OF_DAY, H +24)
                    cal.add(GregorianCalendar.DAY_OF_MONTH, 7)
                }

                // REQUEST CODE DIFFERENTIATE EACH
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, Receiver::class.java)
                intent.putExtra("Title", "$title")
                val pendingIntent = PendingIntent.getBroadcast(context, usedT, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                usedT += 1
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
            } else if ( i == "Thursday") {
                cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
                cal.set(Calendar.HOUR_OF_DAY, H)
                cal.set(Calendar.MINUTE, M)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                if (cal.timeInMillis < System.currentTimeMillis() || cal.before(GregorianCalendar())) {
                    cal.set(Calendar.HOUR_OF_DAY, H +24)
                    cal.add(GregorianCalendar.DAY_OF_MONTH, 7)
                }

                // REQUEST CODE DIFFERENTIATE EACH
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, Receiver::class.java)
                intent.putExtra("Title", "$title")
                val pendingIntent = PendingIntent.getBroadcast(context, usedT, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                usedT += 1
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
            } else if ( i == "Friday") {
                cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
                cal.set(Calendar.HOUR_OF_DAY, H)
                cal.set(Calendar.MINUTE, M)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                if (cal.timeInMillis < System.currentTimeMillis() || cal.before(GregorianCalendar())) {
                    cal.set(Calendar.HOUR_OF_DAY, H +24)
                    cal.add(GregorianCalendar.DAY_OF_MONTH, 7)
                }

                // REQUEST CODE DIFFERENTIATE EACH
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, Receiver::class.java)
                intent.putExtra("Title", "$title")
                val pendingIntent = PendingIntent.getBroadcast(context, usedT, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                usedT += 1
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
            }  else if ( i == "Saturday") {
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                cal.set(Calendar.HOUR_OF_DAY, H)
                cal.set(Calendar.MINUTE, M)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                if (cal.timeInMillis < System.currentTimeMillis() || cal.before(GregorianCalendar())) {
                    cal.set(Calendar.HOUR_OF_DAY, H +24)
                    cal.add(GregorianCalendar.DAY_OF_MONTH, 7)
                }

                // REQUEST CODE DIFFERENTIATE EACH
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, Receiver::class.java)
                intent.putExtra("Title", "$title")
                val pendingIntent = PendingIntent.getBroadcast(context, usedT, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                usedT += 1
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
            } else if ( i == "Sunday") {
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                cal.set(Calendar.HOUR_OF_DAY, H)
                cal.set(Calendar.MINUTE, M)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)

                if (cal.timeInMillis < System.currentTimeMillis() || cal.before(GregorianCalendar())) {
                    cal.set(Calendar.HOUR_OF_DAY, H +24)
                    cal.add(GregorianCalendar.DAY_OF_MONTH, 7)
                }

                // REQUEST CODE DIFFERENTIATE EACH
                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(context, Receiver::class.java)
                intent.putExtra("Title", "$title")
                val pendingIntent = PendingIntent.getBroadcast(context, usedT, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                usedT += 1
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
            }
            val editor = sharedPreferences.edit()
            editor.putString("Alarm ${slot[0]} End", (usedT-1).toString())
            editor.apply()
        }
//      REMOVE FROM SLOT
        slot.removeAt(0)
        d("bomoh", "slot after remove: $slot")

        endT = usedT
        H = 0
        M = 0
        day.clear()
    }

    private fun reminderAct(reminderView: View) {
        val selectTime = reminderView.findViewById<ImageView>(R.id.imageSelectTime)
        val displayTime = reminderView.findViewById<TextView>(R.id.displayTime)
        val mon = reminderView.findViewById<Button>(R.id.buttonMon)
        val tue = reminderView.findViewById<Button>(R.id.buttonTue)
        val wed = reminderView.findViewById<Button>(R.id.buttonWed)
        val thu = reminderView.findViewById<Button>(R.id.buttonThu)
        val fri = reminderView.findViewById<Button>(R.id.buttonFri)
        val sat = reminderView.findViewById<Button>(R.id.buttonSat)
        val sun = reminderView.findViewById<Button>(R.id.buttonSun)

        fun act(Day: String, button: Button) {
            if (day.contains(Day)) {
                day.remove(Day)
                d("bomoh", "$Day un-pressed")
                button.setBackgroundColor(Color.WHITE)
            } else {
                day.add(Day)
                d("bomoh", "$Day pressed")
                button.setBackgroundColor(Color.GRAY)
            }
        }

        mon.setOnClickListener { act("Monday", mon) }
        tue.setOnClickListener { act("Tuesday", tue) }
        wed.setOnClickListener { act("Wednesday", wed) }
        thu.setOnClickListener { act("Thursday", thu) }
        fri.setOnClickListener { act("Friday", fri) }
        sat.setOnClickListener { act("Saturday", sat) }
        sun.setOnClickListener { act("Sunday", sun) }

        selectTime.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                val time = SimpleDateFormat("H:mm").format(cal.time)
                H = SimpleDateFormat("H").format(cal.time).toInt()
                M = SimpleDateFormat("mm").format(cal.time).toInt()

                d("bomoh", "time: $time, H:$H, M:$M")
                displayTime.text = time.toString()
            }
            TimePickerDialog(it.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}

class Receiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra("Title")
        d("bomoh", "Receiver: ${Date()} || $title || this is Alarm")
        val CHANNEL_ID = "channel_id_mysugarbud"
        val notificationId = 101

        fun sendNotification(likedName: String) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My Sugar Bud")
                .setContentText("Reminder: $likedName")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        }

        sendNotification(title.toString())

    }
}
