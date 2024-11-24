package com.goodwy.calendar.helpers

import android.content.Context
import com.goodwy.calendar.extensions.eventsHelper
import com.goodwy.calendar.interfaces.WeeklyCalendar
import com.goodwy.calendar.models.Event
import com.goodwy.commons.helpers.DAY_SECONDS
import com.goodwy.commons.helpers.WEEK_SECONDS

class WeeklyCalendarImpl(val callback: WeeklyCalendar, val context: Context) {
    var mEvents = ArrayList<Event>()

    fun updateWeeklyCalendar(weekStartTS: Long) {
        val endTS = weekStartTS + 2 * WEEK_SECONDS
        context.eventsHelper.getEvents(weekStartTS - DAY_SECONDS, endTS) {
            mEvents = it
            callback.updateWeeklyCalendar(it)
        }
    }
}
