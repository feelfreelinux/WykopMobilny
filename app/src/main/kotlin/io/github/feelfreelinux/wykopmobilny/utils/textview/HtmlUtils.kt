package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.URLSpan
import java.net.URLDecoder
import java.util.regex.Pattern

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

fun String.removeSpoilerHtml() : String {
    val regexBegin = "<a href=\"spoiler:"
    val m = Pattern.compile("($regexBegin).*?\">\\[pokaż spoiler]</a>")
            .matcher(this)
    val sb = StringBuffer()
    while (m.find()) {
        val spoilerHtml = m.group(0)
        val text = URLDecoder.decode(
                spoilerHtml.replace(regexBegin, "").replace("\">[pokaż spoiler]</a>", ""),
                "UTF-8")
        m.appendReplacement(sb, text)
    }
    return sb.toString()
}