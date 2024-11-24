package com.goodwy.calendar.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.goodwy.calendar.adapters.EventListWidgetAdapterEmpty

class WidgetServiceEmpty : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent) = EventListWidgetAdapterEmpty(applicationContext)
}
