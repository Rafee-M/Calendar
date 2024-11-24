package com.goodwy.calendar.extensions

import com.goodwy.calendar.helpers.Formatter
import com.goodwy.calendar.models.Event

fun Long.isTsOnProperDay(event: Event): Boolean {
    val dateTime = Formatter.getDateTimeFromTS(this)
    val power = 1 shl (dateTime.dayOfWeek - 1)
    return event.repeatRule and power != 0
}
