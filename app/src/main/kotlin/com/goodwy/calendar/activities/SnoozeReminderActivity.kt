package com.goodwy.calendar.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goodwy.calendar.extensions.config
import com.goodwy.calendar.extensions.eventsDB
import com.goodwy.calendar.extensions.rescheduleReminder
import com.goodwy.calendar.helpers.EVENT_ID
import com.goodwy.commons.extensions.showPickSecondsDialogHelper
import com.goodwy.commons.helpers.ensureBackgroundThread

class SnoozeReminderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        showPickSecondsDialogHelper(config.snoozeTime, true, cancelCallback = { dialogCancelled() }) {
            ensureBackgroundThread {
                val eventId = intent.getLongExtra(EVENT_ID, 0L)
                val event = eventsDB.getEventOrTaskWithId(eventId)
                config.snoozeTime = it / 60
                rescheduleReminder(event, it / 60)
                runOnUiThread {
                    finishActivity()
                }
            }
        }
    }

    private fun dialogCancelled() {
        finishActivity()
    }

    private fun finishActivity() {
        finish()
        overridePendingTransition(0, 0)
    }
}
