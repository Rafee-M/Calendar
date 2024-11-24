package com.goodwy.calendar.dialogs

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.goodwy.calendar.R
import com.goodwy.calendar.adapters.CheckableColorAdapter
import com.goodwy.calendar.databinding.DialogSelectColorBinding
import com.goodwy.calendar.views.AutoGridLayoutManager
import com.goodwy.commons.extensions.getAlertDialogBuilder
import com.goodwy.commons.extensions.setupDialogStuff
import com.goodwy.commons.extensions.viewBinding

class SelectEventColorDialog(val activity: Activity, val colors: IntArray, var currentColor: Int, val callback: (color: Int) -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding by activity.viewBinding(DialogSelectColorBinding::inflate)

    init {
        val colorAdapter = CheckableColorAdapter(activity, colors, currentColor) { color ->
            callback(color)
            dialog?.dismiss()
        }

        binding.colorGrid.apply {
            val width = activity.resources.getDimensionPixelSize(R.dimen.smaller_icon_size)
            val spacing = activity.resources.getDimensionPixelSize(com.goodwy.commons.R.dimen.small_margin) * 2
            layoutManager = AutoGridLayoutManager(context = activity, itemWidth = width + spacing)
            adapter = colorAdapter
        }

        activity.getAlertDialogBuilder()
            .apply {
                setNeutralButton(R.string.default_calendar_color) { dialog, _ ->
                    callback(0)
                    dialog?.dismiss()
                }

                activity.setupDialogStuff(binding.root, this, R.string.event_color) {
                    dialog = it
                }
            }
    }
}
