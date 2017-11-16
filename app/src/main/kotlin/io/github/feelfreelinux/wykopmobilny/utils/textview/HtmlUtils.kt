package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.URLSpan

fun String.toSpannable(): Spannable {
    @Suppress("DEPRECATION")
    (return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT, null, CodeTagHandler()) as Spannable
     else
        Html.fromHtml(this, null, CodeTagHandler()) as Spannable
    )
}

@Suppress("DEPRECATION")
fun String.removeHtml() : String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else Html.fromHtml(this).toString()
}
