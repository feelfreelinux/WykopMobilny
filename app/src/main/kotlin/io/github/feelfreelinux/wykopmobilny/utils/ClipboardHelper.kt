package io.github.feelfreelinux.wykopmobilny.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import io.github.feelfreelinux.wykopmobilny.R

interface ClipboardHelperApi {
    fun copyTextToClipboard(text: String, label: String = "copied")
}

class ClipboardHelper(val context: Context) : ClipboardHelperApi {
    override fun copyTextToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText(label, text)
        showToast(context.resources.getString(R.string.copied_to_clipboard))
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}