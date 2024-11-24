package com.goodwy.calendar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.goodwy.calendar.extensions.config
import com.goodwy.calendar.extensions.recheckCalDAVCalendars
import com.goodwy.calendar.extensions.refreshCalDAVCalendars
import com.goodwy.calendar.extensions.updateWidgets

class CalDAVSyncReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (context.config.caldavSync) {
            context.refreshCalDAVCalendars(context.config.caldavSyncedCalendarIds, false)
        }

        context.recheckCalDAVCalendars(true) {
            context.updateWidgets()
        }
    }
}
