package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.ParcelableSpan
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.util.Linkify
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder
import io.github.feelfreelinux.wykopmobilny.utils.api.convertMarkdownToHtml
import java.net.URLDecoder

fun TextView.prepareBody(html: String, listener: (String) -> Unit) {
    text = SpannableStringBuilder(html.toSpannable())
    val method = BetterLinkMovementMethod.linkifyHtml(this)
    method.setOnLinkClickListener { _, url ->
        if (!url.text().startsWith("spoiler:")) listener.invoke(url.text())
        true
    }
}

fun TextView.prepareBody(html: String, urlClickListener: (String) -> Unit, clickListener: (() -> Unit)? = null, shouldOpenSpoilerDialog: Boolean) {
    text = SpannableStringBuilder(html.toSpannable())
    val method = BetterLinkMovementMethod.linkifyHtml(this)
    clickListener?.let {
        method.setOnTextClickListener {
            clickListener()
        }
    }
    method.setOnLinkClickListener { _, url ->
        if (url.text().startsWith("spoiler:")) {
            if (!shouldOpenSpoilerDialog) {
                openSpoilers(url.span(), url.text())
            } else {
                val s = SpannableString(URLDecoder.decode(url.text().substringAfter("spoiler:"), "UTF-8"))
                Linkify.addLinks(s, Linkify.WEB_URLS)
                context.createAlertBuilder().apply {
                    setTitle("Spoiler")
                    setMessage(s)
                    setPositiveButton(android.R.string.ok, null)
                    val d = create()
                    d.show()
                    d.findViewById<TextView>(android.R.id.message)?.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        } else urlClickListener(url.text())
        true
    }
}

fun TextView.openSpoilers(span: ClickableSpan, rawText: String) {
    val spoilerText = URLDecoder.decode(rawText.substringAfter("spoiler:"), "UTF-8").convertMarkdownToHtml().toSpannable()
    val textBuilder = (text as SpannableString)
    val start = textBuilder.getSpanStart(span)
    val end = textBuilder.getSpanEnd(span)
    val ssb = SpannableStringBuilder(textBuilder.replaceRange(start, end, spoilerText))
    textBuilder.getSpans(0, textBuilder.length, ParcelableSpan::class.java).forEach {
        val spanStart = textBuilder.getSpanStart(it)
        val spanEnd = textBuilder.getSpanEnd(it)
        val totalStart = if (spanStart < start) spanStart else spanStart - 15 + spoilerText.length
        val totalEnd = if (spanEnd < start) spanEnd else spanEnd - 15 + spoilerText.length
        if (span != it && spanStart != start && spanEnd != end) ssb.setSpan(it, totalStart, totalEnd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    spoilerText.getSpans(0, spoilerText.length, ParcelableSpan::class.java).forEach {
        ssb.setSpan(it, start + spoilerText.getSpanStart(it), start + spoilerText.getSpanEnd(it), Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }

    text = ssb
}

