package com.goodwy.calendar.helpers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.goodwy.calendar.R
import com.goodwy.calendar.activities.SplashActivity
import com.goodwy.calendar.extensions.config
import com.goodwy.commons.extensions.applyColorFilter
import com.goodwy.commons.extensions.getLaunchIntent
import com.goodwy.commons.extensions.setVisibleIf

class MyWidgetDateProvider : AppWidgetProvider() {
    private val OPEN_APP_INTENT_ID = 1

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetManager.getAppWidgetIds(getComponentName(context)).forEach {
            RemoteViews(context.packageName, R.layout.widget_date).apply {
                applyColorFilter(R.id.widget_date_background, context.config.widgetBgColor)
                setTextColor(R.id.widget_date, context.config.widgetSecondTextColor)
                setTextColor(R.id.widget_day_week, context.config.widgetTextColor)
                setTextColor(R.id.widget_month, context.config.widgetSecondTextColor)
                setVisibleIf(R.id.widget_name, context.config.showWidgetName)

                setupAppOpenIntent(context, this)
                appWidgetManager.updateAppWidget(it, this)
            }

            appWidgetManager.notifyAppWidgetViewDataChanged(it, R.id.widget_date_holder)
        }
    }

    private fun getComponentName(context: Context) = ComponentName(context, MyWidgetDateProvider::class.java)

    private fun setupAppOpenIntent(context: Context, views: RemoteViews) {
        (context.getLaunchIntent() ?: Intent(context, SplashActivity::class.java)).apply {
            val pendingIntent = PendingIntent.getActivity(context, OPEN_APP_INTENT_ID, this, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_date_holder, pendingIntent)
        }
    }
}
