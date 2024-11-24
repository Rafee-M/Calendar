package com.goodwy.calendar.dialogs

import com.goodwy.calendar.databinding.DatetimePatternInfoLayoutBinding
import com.goodwy.commons.activities.BaseSimpleActivity
import com.goodwy.commons.extensions.getAlertDialogBuilder
import com.goodwy.commons.extensions.setupDialogStuff
import com.goodwy.commons.extensions.viewBinding

class DateTimePatternInfoDialog(activity: BaseSimpleActivity) {
    val binding by activity.viewBinding(DatetimePatternInfoLayoutBinding::inflate)

    init {
        activity.getAlertDialogBuilder()
            .setPositiveButton(com.goodwy.commons.R.string.ok) { _, _ -> { } }
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }
}
