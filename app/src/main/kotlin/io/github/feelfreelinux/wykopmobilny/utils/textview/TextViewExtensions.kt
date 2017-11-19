package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.text.SpannableStringBuilder
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.createAlertBuilder
import java.net.URLDecoder


fun TextView.prepareBody(html: String, listener : URLClickedListener) {
    text = SpannableStringBuilder(html.toSpannable())
    val method = BetterLinkMovementMethod.linkifyHtml(this)
    method.setOnLinkClickListener({
        _, url ->
        if (url.startsWith("spoiler:")) {
            val text = url.substringAfter("spoiler:")
            context.createAlertBuilder().apply {
                setTitle("Spoiler")
                setMessage(URLDecoder.decode(text, "UTF-8"))
                setPositiveButton(android.R.string.ok, null)
                create().show()
            }
        } else listener.handleUrl(url)
        true
    })
}