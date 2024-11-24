package com.goodwy.calendar.dialogs

import androidx.appcompat.app.AlertDialog
import com.goodwy.calendar.R
import com.goodwy.calendar.activities.SimpleActivity
import com.goodwy.calendar.databinding.DialogImportEventsBinding
import com.goodwy.calendar.extensions.config
import com.goodwy.calendar.extensions.eventTypesDB
import com.goodwy.calendar.extensions.eventsHelper
import com.goodwy.calendar.helpers.IcsImporter
import com.goodwy.calendar.helpers.IcsImporter.ImportResult.IMPORT_FAIL
import com.goodwy.calendar.helpers.IcsImporter.ImportResult.IMPORT_NOTHING_NEW
import com.goodwy.calendar.helpers.IcsImporter.ImportResult.IMPORT_OK
import com.goodwy.calendar.helpers.IcsImporter.ImportResult.IMPORT_PARTIAL
import com.goodwy.calendar.helpers.REGULAR_EVENT_TYPE_ID
import com.goodwy.commons.extensions.*
import com.goodwy.commons.helpers.ensureBackgroundThread

class ImportEventsDialog(val activity: SimpleActivity, val path: String, val callback: (refreshView: Boolean) -> Unit) {
    private var currEventTypeId = REGULAR_EVENT_TYPE_ID
    private var currEventTypeCalDAVCalendarId = 0
    private val config = activity.config
    private val binding by activity.viewBinding(DialogImportEventsBinding::inflate)

    init {
        ensureBackgroundThread {
            if (activity.eventTypesDB.getEventTypeWithId(config.lastUsedLocalEventTypeId) == null) {
                config.lastUsedLocalEventTypeId = REGULAR_EVENT_TYPE_ID
            }

            val isLastCaldavCalendarOK = config.caldavSync && config.getSyncedCalendarIdsAsList().contains(config.lastUsedCaldavCalendarId)
            currEventTypeId = if (isLastCaldavCalendarOK) {
                val lastUsedCalDAVCalendar = activity.eventsHelper.getEventTypeWithCalDAVCalendarId(config.lastUsedCaldavCalendarId)
                if (lastUsedCalDAVCalendar != null) {
                    currEventTypeCalDAVCalendarId = config.lastUsedCaldavCalendarId
                    lastUsedCalDAVCalendar.id!!
                } else {
                    REGULAR_EVENT_TYPE_ID
                }
            } else {
                config.lastUsedLocalEventTypeId
            }
            binding.importEventsCheckbox.isChecked = config.lastUsedIgnoreEventTypesState

            activity.runOnUiThread {
                initDialog()
            }
        }
    }

    private fun initDialog() {
        binding.apply {
            updateEventType(this)
            importEventTypeTitle.setOnClickListener {
                SelectEventTypeDialog(
                    activity = activity,
                    currEventType = currEventTypeId,
                    showCalDAVCalendars = true,
                    showNewEventTypeOption = true,
                    addLastUsedOneAsFirstOption = false,
                    showOnlyWritable = true,
                    showManageEventTypes = false
                ) {
                    currEventTypeId = it.id!!
                    currEventTypeCalDAVCalendarId = it.caldavCalendarId

                    config.lastUsedLocalEventTypeId = it.id!!
                    config.lastUsedCaldavCalendarId = it.caldavCalendarId

                    updateEventType(this)
                }
            }

            importEventsCheckboxHolder.setOnClickListener {
                importEventsCheckbox.toggle()
            }
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(com.goodwy.commons.R.string.ok, null)
            .setNegativeButton(com.goodwy.commons.R.string.cancel, null)
            .apply {
                activity.setupDialogStuff(binding.root, this, R.string.import_events) { alertDialog ->
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(null)
                        activity.toast(com.goodwy.commons.R.string.importing)
                        ensureBackgroundThread {
                            val overrideFileEventTypes = binding.importEventsCheckbox.isChecked
                            config.lastUsedIgnoreEventTypesState = overrideFileEventTypes
                            val result = IcsImporter(activity).importEvents(path, currEventTypeId, currEventTypeCalDAVCalendarId, overrideFileEventTypes)
                            handleParseResult(result)
                            alertDialog.dismiss()
                        }
                    }
                }
            }
    }

    private fun updateEventType(binding: DialogImportEventsBinding) {
        ensureBackgroundThread {
            val eventType = activity.eventTypesDB.getEventTypeWithId(currEventTypeId)
            activity.runOnUiThread {
                binding.importEventTypeTitle.setText(eventType!!.getDisplayTitle())
                binding.importEventTypeColor.setFillWithStroke(eventType.color, activity.getProperBackgroundColor())
            }
        }
    }

    private fun handleParseResult(result: IcsImporter.ImportResult) {
        activity.toast(
            when (result) {
                IMPORT_NOTHING_NEW -> com.goodwy.commons.R.string.no_new_items
                IMPORT_OK -> com.goodwy.commons.R.string.importing_successful
                IMPORT_PARTIAL -> com.goodwy.commons.R.string.importing_some_entries_failed
                else -> com.goodwy.commons.R.string.no_items_found
            }
        )
        callback(result != IMPORT_FAIL)
    }
}
