package com.goodwy.calendar.extensions

import com.goodwy.calendar.models.ListEvent

fun ListEvent.shouldStrikeThrough() = isTaskCompleted || isAttendeeInviteDeclined || isEventCanceled
