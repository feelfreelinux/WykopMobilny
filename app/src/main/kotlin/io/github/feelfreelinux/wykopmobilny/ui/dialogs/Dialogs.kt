package io.github.feelfreelinux.wykopmobilny.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.markdownLink

typealias formatDialogCallback = (String) -> Unit


fun EditTextFormatDialog(titleId : Int, context : Context, callback: formatDialogCallback): AlertDialog? {
    val alertBuilder = context.createAlertBuilder()
    val editText = getEditTextView(context)

    alertBuilder.run {
        setTitle(titleId)
        setView(editText)
        setPositiveButton(android.R.string.ok, {_, _ -> callback.invoke(editText.text.toString())})
        return create()
    }
}

fun UploadPhotoDialog(context : Context, galleryUploadCallback: () -> Unit, urlUploadCallback: formatDialogCallback): AlertDialog? {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setTitle(R.string.insert_photo)
        setItems(R.array.upload_image_dialog, {
            _, pos ->
            when(pos) {
                0 -> galleryUploadCallback.invoke()
                1 -> EditTextFormatDialog(R.string.insert_photo_url, context, urlUploadCallback)?.show()
            }
        })
        return create()
    }
}

fun LennyfaceDialog(context : Context, callback: formatDialogCallback): AlertDialog? {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setTitle(R.string.insert_emoticon)
        val lennyArray = context.resources.getStringArray(R.array.lenny_face_array)
        setItems(lennyArray, {
            _, pos -> callback.invoke(lennyArray[pos])
        })
        return create()
    }
}

fun MarkDownLinkDialog(context : Context, callback: formatDialogCallback): AlertDialog? {
    val alertBuilder = context.createAlertBuilder()

    val view = View.inflate(context, R.layout.dialog_insert_link, null)
    val padding = context.resources.getDimension(R.dimen.dialog_edittext_padding).toInt()
    view.setPadding(padding, padding, padding, padding)

    val descriptionEditText = view.findViewById<EditText>(R.id.description)
    val linkEditText = view.findViewById<EditText>(R.id.title)

    alertBuilder.run {
        setTitle(R.string.insert_link)
        setView(view)
        setPositiveButton(android.R.string.ok, {_, _ ->
            callback.invoke(
                    linkEditText.text.toString().markdownLink(
                            descriptionEditText.text.toString()
                    ))})
        return create()
    }
}

fun AppExitConfirmationDialog(context: Context, callback: () -> Unit) : AlertDialog? {
    val alertBuilder = context.createAlertBuilder()

    alertBuilder.run {
        setMessage(R.string.confirm_app_exit)
        setPositiveButton(android.R.string.yes) { _, _ -> callback.invoke() }
        setNegativeButton(android.R.string.no, null)
        setCancelable(true)

        return create()
    }
}


fun getEditTextView(context : Context): EditText {
    val editText = EditText(context)
    val margin = context.resources.getDimension(R.dimen.dialog_edittext_padding).toInt()
    val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

    params.setMargins(margin, margin, margin, margin)
    editText.layoutParams = params
    return editText
}