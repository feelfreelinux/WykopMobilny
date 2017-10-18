package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.URLSpan
import android.view.View
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

fun String.toSpannable(): Spannable {
    @Suppress("DEPRECATION")
    (return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT, null, SpoilerTagHandler()) as Spannable
     else
        Html.fromHtml(this, null, SpoilerTagHandler()) as Spannable
    )
}

fun SpannableStringBuilder.makeLinkClickable(span: URLSpan, handler : WykopLinkHandlerApi) {
    val start = getSpanStart(span)
    val end = getSpanEnd(span)
    val flags = getSpanFlags(span)
    val clickable = object : LinkSpan() {
        override fun onClick(tv: View) {
            handler.handleUrl(span.url)
        }
    }
    setSpan(clickable, start, end, flags)
    removeSpan(span)
}

@Suppress("DEPRECATION")
fun String.removeHtml() : String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else Html.fromHtml(this).toString()
}