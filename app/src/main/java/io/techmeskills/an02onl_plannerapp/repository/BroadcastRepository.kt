package io.techmeskills.an02onl_plannerapp.repository

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import io.techmeskills.an02onl_plannerapp.widget.PlannerAppWidget
import io.techmeskills.an02onl_plannerapp.widget.UpdateWidgetService

class BroadcastRepository(private val context: Context) {
    fun broadcastNotesUpdate() {
        val intent = Intent(context, UpdateWidgetService::class.java)
        intent.action = PlannerAppWidget.APP_WIDGET_SYNC_ACTION
        JobIntentService.enqueueWork(
            context, UpdateWidgetService::class.java,
            PlannerAppWidget.JOB_ID, intent
        )
    }
}