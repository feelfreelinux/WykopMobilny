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

class SpoilerAwareLinkSpan(val url : String) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}