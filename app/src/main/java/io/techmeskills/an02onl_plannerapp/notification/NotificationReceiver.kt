package io.techmeskills.an02onl_plannerapp.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.techmeskills.an02onl_plannerapp.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, arg1: Intent) {
        showNotification(context, arg1)
    }

    private fun showNotification(context: Context, intent: Intent) {
        try {
            val contentIntent = PendingIntent.getActivity(
                context, 0,
                Intent(context, NotificationReceiver::class.java), 0
            )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(notificationManager)
            }

            val noteId = intent.getLongExtra(NOTIFICATION_KEY_NOTE_ID, -1)
            val noteText = intent.getStringExtra(NOTIFICATION_KEY_NOTE_TEXT)
            val noteOwner = intent.getStringExtra(NOTIFICATION_KEY_NOTE_OWNER)

            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Hi, $noteOwner")
                .setContentText("Remind you: $noteText")
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(makeDeleteAction(context, noteId))
                .addAction(makePostponeAction(context, noteId))
                .setDefaults(Notification.DEFAULT_SOUND)

                .setAutoCancel(true)
            notificationManager.notify(0, builder.build())
        } catch (e: Exception) {
            println("===============================")
            e.printStackTrace()
            println("===============================")
        }
    }

    private fun makeDeleteAction(context: Context, noteId: Long): NotificationCompat.Action {
        val deleteIntent =
            Intent(context.applicationContext, NotificationActionService::class.java)
        deleteIntent.action = ACTION_DELETE
        deleteIntent.putExtra(NOTIFICATION_KEY_NOTE_ID, noteId)
        val deletePendingIntent = PendingIntent.getService(
            context.applicationContext,
            1111,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_delete,
            "Delete",
            deletePendingIntent
        ).build()
    }

    private fun makePostponeAction(context: Context, noteId: Long): NotificationCompat.Action {
        val postponeIntent =
            Intent(context.applicationContext, NotificationActionService::class.java)
        postponeIntent.action = ACTION_POSTPONE
        postponeIntent.putExtra(NOTIFICATION_KEY_NOTE_ID, noteId)
        val postponePendingIntent = PendingIntent.getService(
            context.applicationContext,
            1112,
            postponeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action.Builder(
            R.drawable.ic_eye,
            "Postpone",
            postponePendingIntent
        ).build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationManager: NotificationManager) {
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) == null) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                NOTIFICATION_CHANNEL,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val ACTION = "PLANNER_APP_NOTIFICATION"
        private const val NOTIFICATION_CHANNEL = "PLANNER_APP_NOTIFICATION_CHANNEL"
        const val ACTION_DELETE = "PLANNER_APP_NOTIFICATION_DELETE"
        const val ACTION_POSTPONE = "PLANNER_APP_NOTIFICATION_POSTPONE"
        const val NOTIFICATION_KEY_NOTE_ID = "PLANNER_APP_NOTIFICATION_KEY_NOTE_ID"
        const val NOTIFICATION_KEY_NOTE_TEXT = "PLANNER_APP_NOTIFICATION_KEY_NOTE_TEXT"
        const val NOTIFICATION_KEY_NOTE_OWNER = "PLANNER_APP_NOTIFICATION_KEY_NOTE_OWNER"
    }
}