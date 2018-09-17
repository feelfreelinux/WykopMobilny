package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import io.github.feelfreelinux.wykopmobilny.R
import kotlinx.android.synthetic.main.year_picker.view.*
import java.util.Calendar

class YearPickerDialog : androidx.fragment.app.DialogFragment() {

    companion object {
        const val RESULT_CODE = 167
        const val EXTRA_YEAR = "EXTRA_YEAR"

        fun newInstance(selectedYear: Int = 0): YearPickerDialog {
            val intent = YearPickerDialog()
            val arguments = Bundle()
            arguments.putInt(EXTRA_YEAR, selectedYear)
            intent.arguments = arguments
            return intent
        }
    }

    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private var yearSelection = currentYear

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val argumentYear = arguments!!.getInt(EXTRA_YEAR)
        yearSelection = if (argumentYear == 0) currentYear else argumentYear
        val dialogView = View.inflate(context, R.layout.year_picker, null)
        dialogBuilder.apply {
            setPositiveButton(android.R.string.ok) { _, _ ->
                val data = Intent()
                data.putExtra(EXTRA_YEAR, yearSelection)
                targetFragment?.onActivityResult(targetRequestCode, RESULT_CODE, data)
            }
            setView(dialogView)
        }
        val newDialog = dialogBuilder.create()
        newDialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.apply {
            yearPicker.minValue = 2006
            yearPicker.maxValue = currentYear
            yearPicker.value = yearSelection
            yearTextView.text = yearSelection.toString()

            yearPicker.setOnValueChangedListener { _, _, year ->
                yearSelection = year
                yearTextView.text = year.toString()

            }
        }
        return newDialog
    }
}