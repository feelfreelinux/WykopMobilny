package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.markdownLink
import kotlinx.android.synthetic.main.dialog_edittext.view.*
import kotlinx.android.synthetic.main.dialog_insert_link.view.*

typealias formatDialogCallback = (String) -> Unit

fun EditTextFormatDialog(titleId : Int, context : Context, callback: formatDialogCallback): AlertDialog {
    val alertBuilder = context.createAlertBuilder()
    val editTextLayout = getEditTextView(context)

    alertBuilder.run {
        setTitle(titleId)
        setView(editTextLayout)
        setPositiveButton(android.R.string.ok) { _, _ -> callback.invoke(editTextLayout.editText.text.toString())}
        return create()
    }
}

fun LennyfaceDialog(context : Context, callback: formatDialogCallback): AlertDialog {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setTitle(R.string.insert_emoticon)
        val lennyArray = context.resources.getStringArray(R.array.lenny_face_array)
        setItems(lennyArray) { _, pos -> callback.invoke(lennyArray[pos]) }
        return create()
    }
}

fun MarkDownLinkDialog(context : Context, callback: formatDialogCallback): AlertDialog {
    val alertBuilder = context.createAlertBuilder()

    val view = View.inflate(context, R.layout.dialog_insert_link, null)
    val padding = context.resources.getDimension(R.dimen.dialog_edittext_padding).toInt()
    view.setPadding(padding, padding, padding, padding)


    alertBuilder.run {
        setTitle(R.string.insert_link)
        setView(view)
        setPositiveButton(android.R.string.ok) { _, _ ->
            val link = view.link.text.toString()
            val description = view.description.text.toString()
            callback.invoke(link.markdownLink(description))
        }
        return create()
    }
}

fun ConfirmationDialog(context: Context, callback: () -> Unit) : AlertDialog {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setMessage(context.resources.getString(R.string.confirmation))
        setPositiveButton(android.R.string.yes) { _, _ -> callback.invoke() }
        setNegativeButton(android.R.string.no, null)
        setCancelable(true)

        return create()
    }
}

fun getEditTextView(context : Context): View {
    return View.inflate(context, R.layout.dialog_edittext, null)
}