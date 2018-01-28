package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.Window
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import kotlinx.android.synthetic.main.year_month_picker.*
import kotlinx.android.synthetic.main.year_picker.view.*
import java.util.*

class YearPickerDialog : DialogFragment() {
    companion object {
        val RESULTCODE = 167
        val EXTRA_YEAR = "EXTRA_YEAR"
        val EXTRA_MONTH = "EXTRA_MONTH"
        fun newInstance() : YearPickerDialog {
            return YearPickerDialog()
        }
    }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var yearSelection = currentYear

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)

        val dialogView = View.inflate(context, R.layout.year_picker, null)
        dialogBuilder.apply {
            setPositiveButton(android.R.string.ok, {
                _, _ ->
                val data = Intent()
                data.putExtra(EXTRA_YEAR, yearSelection)
                targetFragment?.onActivityResult(targetRequestCode, RESULTCODE, data)
            })
            setView(dialogView)
        }
        val newDialog = dialogBuilder.create()
        newDialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.apply {
            yearPicker.minValue = 2005
            yearPicker.maxValue = currentYear
            yearPicker.value = currentYear
            yearTextView.text = currentYear.toString()

            yearPicker.setOnValueChangedListener {
                _, _, year ->
                yearSelection = year
                yearTextView.text = year.toString()

            }
        }
        return newDialog
    }
}