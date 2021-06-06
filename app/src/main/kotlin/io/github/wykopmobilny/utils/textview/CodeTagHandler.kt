package io.github.wykopmobilny.utils.textview

import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.style.CharacterStyle
import android.text.style.TypefaceSpan
import org.xml.sax.XMLReader

class CodeTagHandler : Html.TagHandler {
    override fun handleTag(opening: Boolean, tag: String?, output: Editable?, reader: XMLReader?) {
        if (tag.equals("code", true)) {
            val len = output?.length
            if (opening) {
                output?.setSpan(TypefaceSpan("monospace"), len!!, len, Spannable.SPAN_MARK_MARK)
            } else {
                val obj = getLast(output!!, CharacterStyle::class.java)
                obj?.let {
                    val where = output.getSpanStart(obj)

                    if (obj is TypefaceSpan) {
                        output.setSpan(TypefaceSpan("monospace"), where, len!!, 0)
                    }
                }
            }
        }
    }

    private fun getLast(text: Editable, kind: Class<*>): Any? {
        val objs = text.getSpans(0, text.length, kind)

        return if (objs.isEmpty()) {
            null
        } else {
            (objs.size downTo 1)
                .firstOrNull { text.getSpanFlags(objs[it - 1]) == Spannable.SPAN_MARK_MARK }
                ?.let { objs[it - 1] }
        }
    }
}
