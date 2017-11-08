package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder

class SpoilerAwareLinkSpan(val url : String, private val urlClickedListener : (String) -> Unit) : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }

    override fun onClick(view: View) {
        if (url.startsWith("spoiler:")) {
            view.context.createAlertBuilder().run {
                setTitle("Spoiler")
                setMessage(url.substringAfter("spoiler:"))
                create().show()
            }
        } else urlClickedListener(url)
    }
}