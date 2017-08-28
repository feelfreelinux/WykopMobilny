package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.style.URLSpan

fun String.toSpannable(): Spannable {
    @Suppress("DEPRECATION")
    (return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT, null, SpoilerTagHandler()) as Spannable
     else
        Html.fromHtml(this, null, SpoilerTagHandler()) as Spannable
    )
}

const val SPAN_TAG = 0
const val SPAN_PROFILE = 1
const val SPAN_LINK = 2

val URLSpan.type
    get() = when (url.first()) {
        '#'  -> SPAN_TAG
        '@'  -> SPAN_PROFILE
        else -> SPAN_LINK
    }
