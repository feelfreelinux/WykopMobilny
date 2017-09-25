package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

fun TextView.prepareBody(html: String, handler : WykopLinkHandlerApi) {
    val sequence = html.toSpannable()
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(0, strBuilder.length, URLSpan::class.java)
    urls.forEach {
        span -> strBuilder.makeLinkClickable(span, handler)
    }
    text = strBuilder
    movementMethod = LinkMovementMethod.getInstance()
}