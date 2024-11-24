package com.goodwy.calendar.interfaces

import com.goodwy.calendar.models.Event

interface WeeklyCalendar {
    fun updateWeeklyCalendar(events: ArrayList<Event>)
}
