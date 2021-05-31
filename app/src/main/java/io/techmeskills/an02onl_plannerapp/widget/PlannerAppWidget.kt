package io.techmeskills.an02onl_plannerapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import io.techmeskills.an02onl_plannerapp.MainActivity
import io.techmeskills.an02onl_plannerapp.R
import java.text.SimpleDateFormat
import java.util.*

class PlannerAppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        val remoteViews = RemoteViews(context.packageName, R.layout.note_list_item_widget)

        remoteViews.setOnClickPendingIntent(R.id.ivSync, getSyncRequestIntent(context))
        remoteViews.setOnClickPendingIntent(R.id.widgetFrame, getStartActivityIntent(context))

        appWidgetManager?.updateAppWidget(appWidgetIds, remoteViews)

        startSync(context)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val remoteViews = RemoteViews(context.packageName, R.layout.note_list_item_widget)
        val componentWidget = ComponentName(context, PlannerAppWidget::class.java)

        when (intent?.action) {
            APP_WIDGET_SYNC_ACTION -> handleSyncRequest(context, remoteViews)
            APP_WIDGET_SYNC_RESULT -> handleSyncResult(remoteViews, intent)
            APP_WIDGET_OPEN_APP -> {
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        }

        val ids = appWidgetManager.getAppWidgetIds(componentWidget)

        remoteViews.setOnClickPendingIntent(R.id.ivSync, getSyncRequestIntent(context))
        remoteViews.setOnClickPendingIntent(R.id.widgetFrame, getStartActivityIntent(context))

        remoteViews.setOnClickPendingIntent(
            R.id.widgetFrame,
            PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)
        )

        appWidgetManager.updateAppWidget(ids, remoteViews)
    }

    private fun handleSyncRequest(context: Context, remoteViews: RemoteViews) {
        startSync(context)
    }

    private fun handleSyncResult(remoteViews: RemoteViews, intent: Intent?) {
        remoteViews.setTextViewText(R.id.tvTitle, intent?.getStringExtra(KEY_NOTE_TEXT))
        val time: Long = intent?.getLongExtra(KEY_NOTE_TIME, 0L) ?: 0L
        if (time > 0) {
            remoteViews.setTextViewText(R.id.tvDate, dateFormatter.format(Date(time)))
        } else {
            remoteViews.setTextViewText(R.id.tvDate, "")
        }
    }

    private fun getSyncRequestIntent(context: Context?): PendingIntent? {
        val intent = Intent(context, javaClass)
        intent.action = APP_WIDGET_SYNC_ACTION
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    private fun getStartActivityIntent(context: Context?): PendingIntent? {
        val intent = Intent(context, javaClass)
        intent.action = APP_WIDGET_OPEN_APP
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    private fun startSync(context: Context) {
        val intent = Intent(context, UpdateWidgetService::class.java)
        intent.action = APP_WIDGET_SYNC_ACTION
        JobIntentService.enqueueWork(context, UpdateWidgetService::class.java, JOB_ID, intent)
    }

    companion object {
        private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        const val APP_WIDGET_OPEN_APP = "PLANNER_APP_WIDGET_OPEN_APP"
        const val APP_WIDGET_SYNC_ACTION = "PLANNER_APP_WIDGET_SYNC_ACTION"
        const val APP_WIDGET_SYNC_RESULT = "PLANNER_APP_WIDGET_SYNC_RESULT"
        const val JOB_ID = 14839

        const val KEY_NOTE_TEXT = "PLANNER_APP_KEY_NOTE_TEXT"
        const val KEY_NOTE_TIME = "PLANNER_APP_KEY_NOTE_TIME"
    }
}