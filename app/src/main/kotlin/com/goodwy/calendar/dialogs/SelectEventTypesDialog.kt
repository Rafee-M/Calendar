package com.goodwy.calendar.dialogs

import androidx.appcompat.app.AlertDialog
import com.goodwy.calendar.activities.SimpleActivity
import com.goodwy.calendar.adapters.FilterEventTypeAdapter
import com.goodwy.calendar.databinding.DialogFilterEventTypesBinding
import com.goodwy.calendar.extensions.eventsHelper
import com.goodwy.commons.extensions.getAlertDialogBuilder
import com.goodwy.commons.extensions.setupDialogStuff
import com.goodwy.commons.extensions.viewBinding

class SelectEventTypesDialog(val activity: SimpleActivity, selectedEventTypes: Set<String>, val callback: (HashSet<String>) -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogFilterEventTypesBinding::inflate)

    init {
        activity.eventsHelper.getEventTypes(activity, false) {
            binding.filterEventTypesList.adapter = FilterEventTypeAdapter(activity, it, selectedEventTypes)

            activity.getAlertDialogBuilder()
                .setPositiveButton(com.goodwy.commons.R.string.ok) { _, _ -> confirmEventTypes() }
                .setNegativeButton(com.goodwy.commons.R.string.cancel, null)
                .apply {
                    activity.setupDialogStuff(binding.root, this) { alertDialog ->
                        dialog = alertDialog
                    }
                }
        }
    }

    private fun confirmEventTypes() {
        val adapter = binding.filterEventTypesList.adapter as FilterEventTypeAdapter
        val selectedItems = adapter.getSelectedItemsList()
            .map { it.toString() }
            .toHashSet()
        callback(selectedItems)
        dialog?.dismiss()
    }
}
