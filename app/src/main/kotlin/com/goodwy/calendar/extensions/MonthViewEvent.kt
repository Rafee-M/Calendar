package com.goodwy.calendar.extensions

import com.goodwy.calendar.models.MonthViewEvent

fun MonthViewEvent.shouldStrikeThrough() = isTaskCompleted || isAttendeeInviteDeclined || isEventCanceled
