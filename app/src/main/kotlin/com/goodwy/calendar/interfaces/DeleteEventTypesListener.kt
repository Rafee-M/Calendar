package com.goodwy.calendar.interfaces

import com.goodwy.calendar.models.EventType

interface DeleteEventTypesListener {
    fun deleteEventTypes(eventTypes: ArrayList<EventType>, deleteEvents: Boolean): Boolean
}
