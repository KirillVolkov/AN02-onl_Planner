package io.techmeskills.an02onl_plannerapp.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import androidx.core.app.JobIntentService
import io.techmeskills.an02onl_plannerapp.repository.NotesRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class UpdateWidgetService : JobIntentService(), KoinComponent {

    private val notesRepository: NotesRepository by inject()

    override fun onHandleWork(intent: Intent) {
        val closestNote = notesRepository.getClosestNote()
        val text = closestNote?.title ?: "No closest events"
        val time = closestNote?.date ?: 0L
        updateWidget(text, time)
    }

    private fun updateWidget(text: String, time: Long) {
        val intent = Intent(applicationContext, PlannerAppWidget::class.java)
        intent.action = PlannerAppWidget.APP_WIDGET_SYNC_RESULT

        intent.putExtra(PlannerAppWidget.KEY_NOTE_TEXT, text)
        intent.putExtra(PlannerAppWidget.KEY_NOTE_TIME, time)

        val widgetManager = AppWidgetManager.getInstance(applicationContext)
        val ids = widgetManager.getAppWidgetIds(
            ComponentName(
                applicationContext,
                PlannerAppWidget::class.java
            )
        )
        widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        applicationContext.sendBroadcast(intent)
    }
}