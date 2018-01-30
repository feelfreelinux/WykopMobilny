package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.ParcelableSpan
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder
import io.github.feelfreelinux.wykopmobilny.utils.printout
import java.net.URLDecoder
import java.sql.Struct


fun TextView.prepareBody(html: String, listener : URLClickedListener) {
    text = SpannableStringBuilder(html.toSpannable())
    val method = BetterLinkMovementMethod.linkifyHtml(this)
    method.setOnLinkClickListener({
        textView, url ->
        if (url.text().startsWith("spoiler:")) {
            val text = url.text().substringAfter("spoiler:")

        } else listener.handleUrl(url.text())
        true
    })
}

fun TextView.prepareBody(html: String, urlClickListener : (String) -> Unit, clickListener : (() -> Unit)? = null, shouldOpenSpoilerDialog : Boolean) {
    text = SpannableStringBuilder(html.toSpannable())
    val method = BetterLinkMovementMethod.linkifyHtml(this)
    clickListener?.let {
        method.setOnTextClickListener {
            clickListener()
        }
    }
    method.setOnLinkClickListener({
        _, url ->
        if (url.text().startsWith("spoiler:")) {
            if (!shouldOpenSpoilerDialog) {
                openSpoilers(url.span(), url.text())
            } else {
                context.createAlertBuilder().apply {
                    setTitle("Spoiler")
                    setMessage(URLDecoder.decode(url.text().substringAfter("spoiler:"), "UTF-8"))
                    setPositiveButton(android.R.string.ok, null)
                    create().show()
                }
            }
        } else urlClickListener(url.text())
        true
    })
}

fun TextView.openSpoilers(span : ClickableSpan, rawText : String) {
    val spoilerText = URLDecoder.decode(rawText.substringAfter("spoiler:"), "UTF-8").toSpannable()
    val textBuilder = (text as SpannableString)
    val start = textBuilder.getSpanStart(span)
    val end = textBuilder.getSpanEnd(span)
    val ssb = SpannableStringBuilder(textBuilder.replaceRange(start, end, spoilerText))
    textBuilder.getSpans(0, textBuilder.length, ParcelableSpan::class.java).forEach {
        val spanStart = textBuilder.getSpanStart(it)
        val spanEnd = textBuilder.getSpanEnd(it)
        val totalStart = if (spanStart < start) spanStart else spanStart - 15 + spoilerText.length
        val totalEnd = if (spanEnd < end) spanEnd else spanEnd - 15 + spoilerText.length
        if (span != it) ssb.setSpan(it, totalStart, totalEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    spoilerText.getSpans(0, spoilerText.length, ParcelableSpan::class.java).forEach {
        val spanStart = spoilerText.getSpanStart(it)
        ssb.setSpan(it, start + spanStart, end + spanStart, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
    text = ssb
}