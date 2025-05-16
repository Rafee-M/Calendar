package com.goodwy.calendar.dialogs

import androidx.appcompat.app.AlertDialog
import com.goodwy.calendar.R
import com.goodwy.calendar.activities.SimpleActivity
import com.goodwy.calendar.databinding.DialogSelectHolidayTypesBinding
import com.goodwy.calendar.models.HolidayInfo
import com.goodwy.commons.extensions.beVisibleIf
import com.goodwy.commons.extensions.getAlertDialogBuilder
import com.goodwy.commons.extensions.setupDialogStuff
import com.goodwy.commons.extensions.viewBinding
import com.goodwy.commons.helpers.MEDIUM_ALPHA
import com.goodwy.commons.views.MyAppCompatCheckbox

class SelectHolidayTypesDialog(
    private val activity: SimpleActivity,
    private val holidayInfo: HolidayInfo,
    private val callback: (Set<String>) -> Unit,
) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogSelectHolidayTypesBinding::inflate)
    private val pathsToImport = mutableSetOf<String>()

    init {
        if (holidayInfo.regional.isNullOrEmpty() && holidayInfo.other.isNullOrEmpty()) {
            callback(setOf(holidayInfo.public))
        } else {
            showHolidayTypesDialog()
        }
    }

    private fun showHolidayTypesDialog() {
        binding.publicCheckboxHolder.beVisibleIf(holidayInfo.public.isNotEmpty())
        binding.publicCheckboxHolder.setOnClickListener {
            onHolidayTypeClicked(binding.publicCheckbox, holidayInfo.public)
        }

        binding.regionalCheckboxHolder.beVisibleIf(!holidayInfo.regional.isNullOrEmpty())
        binding.regionalCheckboxHolder.setOnClickListener {
            onHolidayTypeClicked(binding.regionalCheckbox, holidayInfo.regional!!)
        }

        binding.otherCheckboxHolder.beVisibleIf(!holidayInfo.other.isNullOrEmpty())
        binding.otherCheckboxHolder.setOnClickListener {
            onHolidayTypeClicked(binding.otherCheckbox, holidayInfo.other!!)
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.goodwy.commons.R.string.ok) { _, _ ->
                callback(pathsToImport)
                dialog?.dismiss()
            }
            .setNegativeButton(com.goodwy.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(
                    view = binding.root,
                    dialog = this,
                    titleId = R.string.select_holidays_to_import
                ) { alertDialog ->
                    dialog = alertDialog
                    binding.publicCheckboxHolder.performClick()
                }
            }
    }

    private fun onHolidayTypeClicked(checkbox: MyAppCompatCheckbox, path: String) {
        if (pathsToImport.contains(path)) {
            pathsToImport.remove(path)
        } else {
            pathsToImport.add(path)
        }

        checkbox.isChecked = pathsToImport.contains(path)

        if (pathsToImport.isEmpty()) {
            dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                isEnabled = false
                alpha = MEDIUM_ALPHA
            }
        } else {
            dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.apply {
                isEnabled = true
                alpha = 1f
            }
        }
    }
}
