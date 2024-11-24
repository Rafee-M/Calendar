package com.goodwy.calendar.interfaces

import android.util.SparseArray
import com.goodwy.calendar.models.DayYearly

interface YearlyCalendar {
    fun updateYearlyCalendar(events: SparseArray<ArrayList<DayYearly>>, hashCode: Int)
}
