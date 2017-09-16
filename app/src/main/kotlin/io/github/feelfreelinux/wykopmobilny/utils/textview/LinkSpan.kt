package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.TextPaint
import android.text.style.ClickableSpan

abstract class LinkSpan : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}