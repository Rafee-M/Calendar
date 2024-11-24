package com.goodwy.calendar.dialogs

import android.app.Activity
import com.goodwy.calendar.databinding.DialogVerticalLinearLayoutBinding
import com.goodwy.calendar.databinding.MyCheckboxBinding
import com.goodwy.calendar.extensions.withFirstDayOfWeekToFront
import com.goodwy.commons.extensions.getAlertDialogBuilder
import com.goodwy.commons.extensions.setupDialogStuff
import com.goodwy.commons.extensions.viewBinding
import com.goodwy.commons.views.MyAppCompatCheckbox

class RepeatRuleWeeklyDialog(val activity: Activity, val curRepeatRule: Int, val callback: (repeatRule: Int) -> Unit) {
    private val binding by activity.viewBinding(DialogVerticalLinearLayoutBinding::inflate)

    init {
        val days = activity.resources.getStringArray(com.goodwy.commons.R.array.week_days)
        var checkboxes = ArrayList<MyAppCompatCheckbox>(7)
        for (i in 0..6) {
            val pow = 1 shl i
            MyCheckboxBinding.inflate(activity.layoutInflater).root.apply {
                isChecked = curRepeatRule and pow != 0
                text = days[i]
                id = pow
                checkboxes.add(this)
            }
        }

        checkboxes = activity.withFirstDayOfWeekToFront(checkboxes)
        checkboxes.forEach {
            binding.dialogVerticalLinearLayout.addView(it)
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.goodwy.commons.R.string.ok) { _, _ -> callback(getRepeatRuleSum()) }
            .setNegativeButton(com.goodwy.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }

    private fun getRepeatRuleSum(): Int {
        var sum = 0
        val cnt = binding.dialogVerticalLinearLayout.childCount
        for (i in 0 until cnt) {
            val child = binding.dialogVerticalLinearLayout.getChildAt(i)
            if (child is MyAppCompatCheckbox) {
                if (child.isChecked)
                    sum += child.id
            }
        }
        return sum
    }
}
