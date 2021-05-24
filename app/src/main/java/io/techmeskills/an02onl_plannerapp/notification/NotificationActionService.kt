package io.techmeskills.an02onl_plannerapp.notification

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import io.techmeskills.an02onl_plannerapp.repository.NotesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class NotificationActionService : Service(), KoinComponent {

    private val notesRepository: NotesRepository by inject()

    private var noteId: Long = -1

    override fun onBind(p0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            noteId = it.getLongExtra(NotificationReceiver.NOTIFICATION_KEY_NOTE_ID, -1)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            when (it.action) {
                NotificationReceiver.ACTION_DELETE -> {
                    GlobalScope.launch {
                        notesRepository.deleteNoteById(noteId)
                    }
                    notificationManager.cancel(0)
                }
                NotificationReceiver.ACTION_POSTPONE -> {
                    GlobalScope.launch {
                        notesRepository.postponeNoteById(noteId)
                    }
                    notificationManager.cancel(0)
                }
                else -> Unit
            }
        }
        stopSelf()
        return START_NOT_STICKY
    }

}