package com.goodwy.calendar.fragments

import android.graphics.Color
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.goodwy.calendar.databinding.DatePickerDarkBinding
import com.goodwy.calendar.databinding.DatePickerLightBinding
import com.goodwy.commons.extensions.getContrastColor
import com.goodwy.commons.extensions.getProperBackgroundColor
import org.joda.time.DateTime

abstract class MyFragmentHolder : Fragment() {
    abstract val viewType: Int

    abstract fun goToToday()

    abstract fun showGoToDateDialog()

    abstract fun refreshEvents()

    abstract fun shouldGoToTodayBeVisible(): Boolean

    abstract fun getNewEventDayCode(): String

    abstract fun printView()

    abstract fun getCurrentDate(): DateTime?

    fun getDatePickerView(): DatePicker {
        return if (requireActivity().getProperBackgroundColor().getContrastColor() == Color.WHITE) {
            DatePickerDarkBinding.inflate(layoutInflater).datePicker
        } else {
            DatePickerLightBinding.inflate(layoutInflater).datePicker
        }
    }
}
