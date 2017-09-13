package io.github.feelfreelinux.wykopmobilny.utils.textview

import android.app.AlertDialog
import android.os.Build
import android.text.*
import android.text.style.ClickableSpan
import android.view.View
import org.xml.sax.XMLReader
import android.text.style.StrikethroughSpan
import android.text.style.CharacterStyle
import android.text.style.TypefaceSpan


class SpoilerTagHandler : Html.TagHandler {
    @Suppress("DEPRECATION")
    class SpoilerSpan(private val spoilerText : String) : ClickableSpan() {
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
                if (reader?.getAttribute("class").equals("dnone", true))
                    output?.setSpan(StrikethroughSpan(), len!!, len, Spannable.SPAN_MARK_MARK)
                else
                    output?.setSpan(TypefaceSpan("monospace"), len!!, len, Spannable.SPAN_MARK_MARK)

            } else {
                val obj = getLast(output!!, CharacterStyle::class.java)
                obj?.let {
                    val where = output.getSpanStart(obj)

                    if (obj is StrikethroughSpan) {
                        val spoilerTitle = "[pokaż spoiler]"
                        val spoilerText = output.substring(where, len!!)
                        output.removeSpan(obj)
                        output.replace(where, len, spoilerTitle)

                        if (where != len)
                            output.setSpan(SpoilerSpan(spoilerText), where, len - ((len - where) - spoilerTitle.length), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    } else if (obj is TypefaceSpan)
                        output.setSpan(TypefaceSpan("monospace"), where, len!!, 0)
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

    @Suppress("UNCHECKED_CAST")
    private fun XMLReader.getAttribute(attr: String): String? {
        val elementField = javaClass.getDeclaredField("theNewElement")
        elementField.isAccessible = true
        val element = elementField.get(this)
        element?.let {
            val attsField = element.javaClass.getDeclaredField("theAtts")
            attsField.isAccessible = true
            val atts = attsField.get(element)
            val dataField = atts.javaClass.getDeclaredField("data")
            dataField.isAccessible = true
            val data = dataField.get(atts) as Array<String>
            val lengthField = atts.javaClass.getDeclaredField("length")
            lengthField.isAccessible = true
            val len = lengthField.get(atts) as Int
            (0 until len)
                    .filter { attr == data[it * 5 + 1] }
                    .forEach { return data[it * 5 + 4] }
        }
        return null
    }
}