package com.goodwy.calendar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.goodwy.calendar.extensions.*
import com.goodwy.commons.helpers.ensureBackgroundThread

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ensureBackgroundThread {
            context.apply {
                scheduleAllEvents()
                notifyRunningEvents()
                recheckCalDAVCalendars(true) {}
                scheduleNextAutomaticBackup()
                checkAndBackupEventsOnBoot()
            }
        }
    }
}
