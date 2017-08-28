package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

interface EntryTextViewCallbacks {
    fun onTagClicked(tag : String)
    fun onProfileClicked(profile : String)
    fun onLinkClicked(link : String)
}

fun SpannableStringBuilder.makeLinkClickable(span: URLSpan, callbacks : EntryTextViewCallbacks) {
    val start = getSpanStart(span)
    val end = getSpanEnd(span)
    val flags = getSpanFlags(span)
    val clickable = object : LinkSpan() {
        override fun onClick(tv: View) {
            when (span.type) {
                SPAN_TAG -> callbacks.onTagClicked(span.url.removePrefix("#"))
                SPAN_LINK -> callbacks.onLinkClicked(span.url)
                SPAN_PROFILE -> callbacks.onProfileClicked(span.url.removePrefix("@"))
            }

        }
    }
    setSpan(clickable, start, end, flags)
    removeSpan(span)
}

fun TextView.prepareBody(html: String, callback : EntryTextViewCallbacks) {
    val sequence = html.toSpannable()
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(0, strBuilder.length, URLSpan::class.java)
    urls.forEach {
        span ->
        strBuilder.makeLinkClickable(span, callback)
    }
    text = strBuilder
    movementMethod = LinkMovementMethod.getInstance()
}