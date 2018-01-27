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
import kotlinx.android.synthetic.main.year_month_picker.view.*
import java.util.*

class MonthYearPickerDialog : DialogFragment() {
    companion object {
        val RESULTCODE = 167
        val EXTRA_YEAR = "EXTRA_YEAR"
        val EXTRA_MONTH = "EXTRA_MONTH"
        fun newInstance() : MonthYearPickerDialog {
            return MonthYearPickerDialog()
        }
    }

    val years by lazy { (2005 .. Calendar.getInstance().get(Calendar.YEAR)).toList() }
    var yearIndex = years.size -1
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1
    var selectedMonth = currentMonth
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)

        val dialogView = View.inflate(context, R.layout.year_month_picker, null)
        dialogBuilder.apply {
            setPositiveButton(android.R.string.ok, {
                _, _ ->
                val data = Intent()
                data.putExtra(EXTRA_YEAR, years[yearIndex])
                data.putExtra(EXTRA_MONTH, selectedMonth)
                targetFragment?.onActivityResult(targetRequestCode, RESULTCODE, data)
            })
            setView(dialogView)
        }
        val newDialog = dialogBuilder.create()
        newDialog.window.requestFeature(Window.FEATURE_NO_TITLE)

        setYear(dialogView)
        dialogView.arrowLeft.setOnClickListener {
            yearIndex -= 1
            setYear(dialogView)
            setTitleDate(dialogView)
        }

        dialogView.arrowRight.setOnClickListener {
            yearIndex += 1
            setYear(dialogView)
            setTitleDate(dialogView)
        }

        (0..12).forEach {
            val monthIndex = it
            getMonthView(dialogView, monthIndex).setOnClickListener {
                selectMonth(dialogView, monthIndex)
                setTitleDate(dialogView)
            }
        }
        setTitleDate(dialogView)
        selectMonth(dialogView, currentMonth)

        return newDialog
    }

    fun setYear(view : View) {
        view.yearTextView.text = years[yearIndex].toString()
        view.arrowRight.isVisibleNoGone = (yearIndex != years.size - 1)
        view.arrowLeft.isVisibleNoGone = (yearIndex != 0)
        if (yearIndex == (years.size - 1)) {
            (0..12).forEach {
                val index = it
                showMonth(view, index, index <= currentMonth)
                if (selectedMonth > currentMonth) selectMonth(view, currentMonth)
            }
        } else if (yearIndex == 0) {
            (0..11).forEach { showMonth(view, it, false) }
            showMonth(view, 12, true)
            selectMonth(view, 12)
        } else {
            (0..12).forEach { showMonth(view, it, true) }
        }
    }

    var View.isVisibleNoGone : Boolean
        get() = visibility == View.VISIBLE
        set(value) { visibility = if (value) View.VISIBLE else View.INVISIBLE  }

    fun showMonth(view: View, index : Int, isVisible : Boolean) {
        getMonthView(view, index).isVisibleNoGone = isVisible
    }

    fun selectMonth(view : View, index: Int) {
        selectedMonth = index
        (0..12).forEach { getMonthView(view, it).setTypeface(null, Typeface.NORMAL) }
        getMonthView(view, index).setTypeface(null, Typeface.BOLD)
    }

    fun getMonthView(view : View, index : Int) : TextView {
        return when(index) {
            0 -> view.wholeYear
            1 -> view.janTv
            2 -> view.febTv
            3 -> view.marTv
            4 -> view.aprTv
            5 -> view.may
            6 -> view.junTv
            7 -> view.julTv
            8 -> view.augTv
            9 -> view.sepTv
            10 -> view.octTv
            11 -> view.novTv
            else -> view.decTv
        }
    }

    fun setTitleDate(view : View) {
        val monthString = getString(when(selectedMonth) {
            0 -> R.string.whole_year
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
        })
        view.monthYearTextView.text = monthString + ", " + years[yearIndex].toString()
    }
}