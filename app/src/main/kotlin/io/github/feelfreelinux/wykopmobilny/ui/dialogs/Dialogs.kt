package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import kotlinx.android.synthetic.main.dialog_edittext.view.*

typealias formatDialogCallback = (String) -> Unit

fun editTextFormatDialog(titleId: Int, context: Context, callback: formatDialogCallback): AlertDialog {
    val editTextLayout = getEditTextView(context)
    context.createAlertBuilder().run {
        setTitle(titleId)
        setView(editTextLayout)
        setPositiveButton(android.R.string.ok) { _, _ -> callback.invoke(editTextLayout.editText.text.toString()) }
        return create()
    }
}

fun lennyfaceDialog(context: Context, callback: formatDialogCallback): AlertDialog {
    context.createAlertBuilder().run {
        setTitle(R.string.insert_emoticon)
        val lennyArray = context.resources.getStringArray(R.array.lenny_face_array)
        setItems(lennyArray) { _, pos -> callback.invoke(lennyArray[pos]) }
        return create()
    }
}

fun confirmationDialog(context: Context, callback: () -> Unit): AlertDialog {
    context.createAlertBuilder().run {
        setMessage(context.resources.getString(R.string.confirmation))
        setPositiveButton(android.R.string.yes) { _, _ -> callback.invoke() }
        setNegativeButton(android.R.string.no, null)
        setCancelable(true)
        return create()
    }
}

private fun getEditTextView(context: Context) = View.inflate(context, R.layout.dialog_edittext, null)