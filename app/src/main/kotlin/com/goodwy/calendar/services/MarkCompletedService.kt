package com.goodwy.calendar.services

import android.app.IntentService
import android.content.Intent
import com.goodwy.calendar.extensions.eventsDB
import com.goodwy.calendar.extensions.updateTaskCompletion
import com.goodwy.calendar.helpers.ACTION_MARK_COMPLETED
import com.goodwy.calendar.helpers.EVENT_ID

class MarkCompletedService : IntentService("MarkCompleted") {

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null && intent.action == ACTION_MARK_COMPLETED) {
            val taskId = intent.getLongExtra(EVENT_ID, 0L)
            val task = eventsDB.getTaskWithId(taskId)
            if (task != null) {
                updateTaskCompletion(task, completed = true)
            }
        }
    }
}
