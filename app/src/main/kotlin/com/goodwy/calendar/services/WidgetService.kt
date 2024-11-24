package com.goodwy.calendar.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.goodwy.calendar.adapters.EventListWidgetAdapter

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapter(applicationContext, intent)
}
