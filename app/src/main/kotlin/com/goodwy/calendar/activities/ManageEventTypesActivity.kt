package com.goodwy.calendar.activities

import android.os.Bundle
import com.goodwy.calendar.R
import com.goodwy.calendar.adapters.ManageEventTypesAdapter
import com.goodwy.calendar.databinding.ActivityManageEventTypesBinding
import com.goodwy.calendar.dialogs.EditEventTypeDialog
import com.goodwy.calendar.extensions.eventsHelper
import com.goodwy.calendar.interfaces.DeleteEventTypesListener
import com.goodwy.calendar.models.EventType
import com.goodwy.commons.extensions.toast
import com.goodwy.commons.extensions.updateTextColors
import com.goodwy.commons.extensions.viewBinding
import com.goodwy.commons.helpers.NavigationIcon
import com.goodwy.commons.helpers.ensureBackgroundThread

class ManageEventTypesActivity : SimpleActivity(), DeleteEventTypesListener {

    private val binding by viewBinding(ActivityManageEventTypesBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupOptionsMenu()

        updateMaterialActivityViews(
            binding.manageEventTypesCoordinator,
            binding.manageEventTypesList,
            useTransparentNavigation = true,
            useTopSearchMenu = false
        )
        setupMaterialScrollListener(binding.manageEventTypesList, binding.manageEventTypesToolbar)

        getEventTypes()
        updateTextColors(binding.manageEventTypesList)
    }

    override fun onResume() {
        super.onResume()
        setupToolbar(binding.manageEventTypesToolbar, NavigationIcon.Arrow)
    }

    private fun showEventTypeDialog(eventType: EventType? = null) {
        EditEventTypeDialog(this, eventType?.copy()) {
            getEventTypes()
        }
    }

    private fun getEventTypes() {
        eventsHelper.getEventTypes(this, false) {
            val adapter = ManageEventTypesAdapter(this, it, this, binding.manageEventTypesList) {
                showEventTypeDialog(it as EventType)
            }
            binding.manageEventTypesList.adapter = adapter
        }
    }

    private fun setupOptionsMenu() {
        binding.manageEventTypesToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add_event_type -> showEventTypeDialog()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }

    override fun deleteEventTypes(eventTypes: ArrayList<EventType>, deleteEvents: Boolean): Boolean {
        if (eventTypes.any { it.caldavCalendarId != 0 }) {
            toast(R.string.unsync_caldav_calendar)
            if (eventTypes.size == 1) {
                return false
            }
        }

        ensureBackgroundThread {
            eventsHelper.deleteEventTypes(eventTypes, deleteEvents)
        }

        return true
    }
}
