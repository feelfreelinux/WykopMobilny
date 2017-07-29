package io.github.feelfreelinux.wykopmobilny.utils

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.style.URLSpan

fun String.toSpannable(): Spannable {
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        return  Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT, null, SpoilerTagHandler()) as Spannable
    }else{
        return Html.fromHtml(this, null, SpoilerTagHandler()) as Spannable
    }
}

val URLSpan.isTag
    get() = url.first() == '#'