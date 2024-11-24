package com.goodwy.calendar.services

import android.app.IntentService
import android.content.Intent
import com.goodwy.calendar.extensions.config
import com.goodwy.calendar.extensions.eventsDB
import com.goodwy.calendar.extensions.rescheduleReminder
import com.goodwy.calendar.helpers.EVENT_ID

class SnoozeService : IntentService("Snooze") {
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val eventId = intent.getLongExtra(EVENT_ID, 0L)
            val event = eventsDB.getEventOrTaskWithId(eventId)
            rescheduleReminder(event, config.snoozeTime)
        }
    }
}
