package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.TextPaint
import android.text.style.URLSpan

interface URLClickedListener {
    fun handleUrl(url : String)
}

class ClickableSpanNoUnderline(val url : String) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}