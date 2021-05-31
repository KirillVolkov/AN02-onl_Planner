package io.techmeskills.an02onl_plannerapp.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.notification.NotificationReceiver
import java.util.*

class NotificationRepository(private val context: Context, private val alarmManager: AlarmManager) {

    fun setNotification(note: Note) {
        val alarmTimeAtUTC = Date(note.date)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmTimeAtUTC.time,
            makeIntent(note)
        )
    }

    fun unsetNotification(note: Note) {
        alarmManager.cancel(makeIntent(note))
    }

    private fun makeIntent(note: Note): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java)
        intent.action = NotificationReceiver.ACTION
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_ID, note.id)
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_OWNER, note.userName)
        intent.putExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_TEXT, note.title)
        return PendingIntent.getBroadcast(context, 0, intent, FLAG_UPDATE_CURRENT)
    }

    fun postponeNoteTimeByFiveMins(note: Note): Note {
        val time = Date(note.date)
        val calendar = Calendar.getInstance()
        calendar.time = time
        calendar.add(Calendar.MINUTE, 5)
        return note.copy(date = calendar.timeInMillis)
    }
}