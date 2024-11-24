package com.goodwy.calendar.extensions

import com.goodwy.calendar.helpers.MONTH
import com.goodwy.calendar.helpers.WEEK
import com.goodwy.calendar.helpers.YEAR

fun Int.isXWeeklyRepetition() = this != 0 && this % WEEK == 0

fun Int.isXMonthlyRepetition() = this != 0 && this % MONTH == 0

fun Int.isXYearlyRepetition() = this != 0 && this % YEAR == 0
