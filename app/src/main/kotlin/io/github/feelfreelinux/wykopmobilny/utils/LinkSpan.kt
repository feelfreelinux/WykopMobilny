package io.github.feelfreelinux.wykopmobilny.utils

import android.text.style.ClickableSpan
import android.text.TextPaint

abstract class LinkSpan : ClickableSpan() {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}