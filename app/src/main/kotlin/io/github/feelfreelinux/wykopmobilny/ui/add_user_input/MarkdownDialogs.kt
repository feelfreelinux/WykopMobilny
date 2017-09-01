package io.github.feelfreelinux.wykopmobilny.ui.add_user_input

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.widget.EditText
import android.widget.LinearLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.markdownLink
import kotlinx.android.synthetic.main.activity_navigation.*

typealias formatDialogCallback = (String) -> Unit
fun EditTextFormatDialog(titleId : Int, context : Context, callback: formatDialogCallback): AlertDialog? {
    val alertBuilder: android.app.AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
    else android.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK)

    val editText = getEditTextView(context)
    alertBuilder.run {
        setTitle(titleId)
        setView(editText)
        setPositiveButton(android.R.string.ok, {_, _ -> callback.invoke(editText.text.toString())})
        return create()
    }
}

fun UploadPhotoDialog(context : Context, galleryUploadCallback: () -> Unit, urlUploadCallback: formatDialogCallback): AlertDialog? {
    val alertBuilder: android.app.AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
    else android.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK)

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
    val alertBuilder: android.app.AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
    else android.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK)

    alertBuilder.run {
        setTitle(R.string.insert_emoticon)
        val lennyArray = context.resources.getStringArray(R.array.lenny_face_array)
        setItems(lennyArray, {
            _, pos -> callback.invoke(lennyArray[pos])
        })
        return create()
    }
}

fun ExitConfirmationDialog(context : Context, callback : () -> Unit) : AlertDialog? {
    val alertBuilder: android.app.AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
    else android.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK)

    alertBuilder.run {
        setTitle(R.string.confirm_exit)
        setPositiveButton(android.R.string.yes, {_, _ -> callback.invoke() })
        setNeutralButton(android.R.string.no, null)
        return create()
    }
}

fun MarkDownLinkDialog(context : Activity, callback: formatDialogCallback): AlertDialog? {
    val alertBuilder: android.app.AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
        android.app.AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
    else android.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK)

    val view = context.layoutInflater.inflate(R.layout.dialog_insert_link, null)
    val descriptionEditText = view.findViewById<EditText>(R.id.title)
    val linkEditText = view.findViewById<EditText>(R.id.description)

    alertBuilder.run {
        setTitle(R.string.insert_link)
        setView(view)
        setPositiveButton(android.R.string.ok, {_, _ ->
            if ((linkEditText.text != null) and (descriptionEditText != null))
                callback.invoke(
                    linkEditText.text.toString().markdownLink(
                        descriptionEditText.text.toString()
                    ))})
        return create()
    }
}


fun getEditTextView(context : Context): EditText {
    val editText = EditText(context)
    editText.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
    return editText
}