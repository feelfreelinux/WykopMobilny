package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import io.github.feelfreelinux.wykopmobilny.R
import kotlinx.android.synthetic.main.year_month_picker.view.*
import java.util.Calendar

class MonthYearPickerDialog : androidx.fragment.app.DialogFragment() {

    companion object {
        const val RESULT_CODE = 167
        const val EXTRA_YEAR = "EXTRA_YEAR"
        const val EXTRA_MONTH = "EXTRA_MONTH"

        fun newInstance(selectedMonth: Int = 0, selectedYear: Int = 0): MonthYearPickerDialog {
            val intent = MonthYearPickerDialog()
            val arguments = Bundle()
            arguments.putInt(EXTRA_YEAR, selectedYear)
            arguments.putInt(EXTRA_MONTH, selectedMonth)
            intent.arguments = arguments
            return intent
        }
    }

    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var yearSelection = currentYear
    private var selectedMonth = currentMonth - 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
        val argumentYear = arguments!!.getInt(EXTRA_YEAR)
        val argumentMonth = arguments!!.getInt(EXTRA_MONTH)

        yearSelection = if (argumentYear == 0) currentYear else argumentYear
        selectedMonth = if (argumentMonth == 0) currentMonth else argumentMonth - 1
        val dialogView = View.inflate(context, R.layout.year_month_picker, null)
        dialogBuilder.apply {
            setPositiveButton(android.R.string.ok) { _, _ ->
                val data = Intent()
                data.putExtra(EXTRA_YEAR, yearSelection)
                data.putExtra(EXTRA_MONTH, selectedMonth + 1)
                targetFragment?.onActivityResult(targetRequestCode, RESULT_CODE, data)
            }
            setView(dialogView)
        }
        val newDialog = dialogBuilder.create()
        newDialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialogView.apply {
            yearPicker.minValue = 2005
            yearPicker.maxValue = currentYear
            yearPicker.value = yearSelection
            setYear(this)
            monthPicker.value = selectedMonth
            yearPicker.setOnValueChangedListener { _, _, year ->
                yearSelection = year
                setYear(this)
            }

            monthPicker.setOnValueChangedListener { _, _, month ->
                selectedMonth = month
                setTitleDate(this)
            }
        }
        return newDialog
    }

    private fun setYear(view: View) {
        view.apply {
            when (yearSelection) {
                currentYear -> {
                    monthPicker.displayedValues = (1..12).map { getMonthString(it) }.toTypedArray()
                    monthPicker.minValue = 0
                    monthPicker.value = currentMonth - 1
                    monthPicker.maxValue = currentMonth - 1
                    selectedMonth = currentMonth - 1
                }
                2005 -> {
                    monthPicker.minValue = 11
                    monthPicker.maxValue = 11
                    monthPicker.value = 11
                    monthPicker.displayedValues = arrayOf(getMonthString(12))
                    selectedMonth = 11
                }
                else -> {
                    monthPicker.displayedValues = (1..12).map { getMonthString(it) }.toTypedArray()
                    monthPicker.minValue = 0
                    monthPicker.maxValue = 11
                }
            }
            setTitleDate(this)
        }
    }

    private fun setTitleDate(view: View) {
        view.monthTextView.text = getMonthString(selectedMonth + 1)
        view.yearTextView.text = yearSelection.toString()
    }

    private fun getMonthString(index: Int): String =
        getString(
            when (index) {
                1 -> R.string.january
                2 -> R.string.february
                3 -> R.string.march
                4 -> R.string.april
                5 -> R.string.may
                6 -> R.string.june
                7 -> R.string.july
                8 -> R.string.august
                9 -> R.string.september
                10 -> R.string.october
                11 -> R.string.novermber
                else -> R.string.december
            }
        )
}