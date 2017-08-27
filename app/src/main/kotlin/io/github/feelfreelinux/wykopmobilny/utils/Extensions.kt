package io.github.feelfreelinux.wykopmobilny.utils

import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.utils.api.parseDate
import io.github.feelfreelinux.wykopmobilny.ui.photoview.launchPhotoView
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.textview.LinkSpan
import io.github.feelfreelinux.wykopmobilny.utils.textview.isTag
import io.github.feelfreelinux.wykopmobilny.utils.textview.toSpannable
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun RecyclerView.prepare() {
    setHasFixedSize(true) // For better performance
    layoutManager = LinearLayoutManager(context)
}

fun SpannableStringBuilder.makeLinkClickable(span: URLSpan, callback : TagClickedListener) {
    val start = getSpanStart(span)
    val end = getSpanEnd(span)
    val flags = getSpanFlags(span)
    val clickable = object : LinkSpan() {
        override fun onClick(tv: View) {
            if (span.isTag)
                callback.invoke(span.url.removePrefix("#"))

        }
    }
    setSpan(clickable, start, end, flags)
    removeSpan(span)
}

fun TextView.prepareBody(html: String, callback : TagClickedListener) {
    val sequence = html.toSpannable()
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(0, strBuilder.length, URLSpan::class.java)
    urls.forEach {
        span ->
        strBuilder.makeLinkClickable(span, callback)
    }
    text = strBuilder
    movementMethod = LinkMovementMethod.getInstance()
}

fun View.disableFor(millis: Long){
    isEnabled = false
    postDelayed({
        isEnabled = true
    }, millis)
}

fun ImageView.loadImage(url : String) {
    GlideApp.with(context)
            .load(url).into(this)
}

fun ImageView.setPhotoViewUrl( url : String) {
    setOnClickListener {
        context.run {
            launchPhotoView(url)
        }
    }
}

inline fun<reified T : Any> LazyKodein.instanceValue() = instance<T>().value

fun String.toPrettyDate() : String = PrettyTime(Locale("pl")).format(parseDate(this))