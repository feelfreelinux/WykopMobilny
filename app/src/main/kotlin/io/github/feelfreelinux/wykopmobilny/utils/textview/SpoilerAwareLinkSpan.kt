package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder
import java.net.URLDecoder

interface URLClickedListener {
    fun handleUrl(url : String)
}

class SpoilerAwareLinkSpan(val url : String, val urlClickedListener : URLClickedListener) : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }

    override fun onClick(view: View) {
        if (url.startsWith("spoiler:")) {
            val text = url.substringAfter("spoiler:")
            view.context.createAlertBuilder().run {
                setTitle("Spoiler")
                setMessage(URLDecoder.decode(text, "UTF-8"))
                create().show()
            }
        } else urlClickedListener.handleUrl(url)
    }
}