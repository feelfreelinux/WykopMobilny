package io.github.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.DialogEdittextBinding
import io.github.wykopmobilny.databinding.DialogInsertLinkBinding
import io.github.wykopmobilny.utils.layoutInflater

typealias FormatDialogCallback = (String) -> Unit
typealias AddRelatedDialogCallback = (String, String) -> Unit

fun editTextFormatDialog(titleId: Int, context: Context, callback: FormatDialogCallback): AlertDialog {
    val editTextLayout = getEditTextView(context)
    context.createAlertBuilder().run {
        setTitle(titleId)
        setView(editTextLayout.root)
        setPositiveButton(android.R.string.ok) { _, _ -> callback.invoke(editTextLayout.editText.text.toString()) }
        return create()
    }
}

fun lennyfaceDialog(context: Context, callback: FormatDialogCallback): AlertDialog {
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
        setPositiveButton(android.R.string.ok) { _, _ -> callback.invoke() }
        setNegativeButton(android.R.string.cancel, null)
        setCancelable(true)
        return create()
    }
}

fun addRelatedDialog(context: Context, callback: AddRelatedDialogCallback): AlertDialog {
    val editTextLayout = DialogInsertLinkBinding.inflate(context.layoutInflater)
    context.createAlertBuilder().run {
        setTitle("Dodaj powiązane")
        setView(editTextLayout.root)
        setPositiveButton(android.R.string.ok) { _, _ ->
            callback(editTextLayout.link.text.toString(), editTextLayout.description.text.toString())
        }
        return create()
    }
}

private fun getEditTextView(context: Context) = DialogEdittextBinding.inflate(context.layoutInflater)
