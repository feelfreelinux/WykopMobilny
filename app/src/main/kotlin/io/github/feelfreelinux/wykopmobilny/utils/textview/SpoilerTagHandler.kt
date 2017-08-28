package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.app.AlertDialog
import android.os.Build
import android.text.*
import android.text.style.ClickableSpan
import android.view.View
import org.xml.sax.XMLReader
import android.text.style.StrikethroughSpan


class SpoilerTagHandler : Html.TagHandler {
    @Suppress("DEPRECATION")
    class SpoilerSpan(val spoilerText : String) : ClickableSpan() {
        override fun onClick(textView: View?) {
            val alertBuilder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                AlertDialog.Builder(textView?.context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
            else AlertDialog.Builder(textView?.context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)

            alertBuilder.run {
                setTitle("Spoiler")
                setMessage(spoilerText)
                create().show()
            }
        }
        override fun updateDrawState(ds: TextPaint?) {
            super.updateDrawState(ds)
            ds?.isUnderlineText = false
        }

    }
    override fun handleTag(opening: Boolean, tag: String?, output: Editable?, reader: XMLReader?) {
        if (tag.equals("code", true)) {

            val len = output?.length
            if (opening) {
                output?.setSpan(StrikethroughSpan(), len!!, len, Spannable.SPAN_MARK_MARK)
            } else {
                val obj = getLast(output!!, StrikethroughSpan::class.java)
                val where = output.getSpanStart(obj)
                val spoilerTitle = "[pokaż spoiler]"
                val spoilerText = output.substring(where, len!!)
                output.removeSpan(obj)
                output.replace(where, len, spoilerTitle)

                if (where != len)
                    output.setSpan(SpoilerSpan(spoilerText), where, len - ((len - where) - spoilerTitle.length), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            }
        }
    }

    private fun getLast(text: Editable, kind: Class<*>): Any? {
        val objs = text.getSpans(0, text.length, kind)

        if (objs.isEmpty()) {
            return null
        } else {
            return (objs.size downTo 1)
                    .firstOrNull { text.getSpanFlags(objs[it - 1]) == Spannable.SPAN_MARK_MARK }
                    ?.let { objs[it - 1] }
        }
    }
}