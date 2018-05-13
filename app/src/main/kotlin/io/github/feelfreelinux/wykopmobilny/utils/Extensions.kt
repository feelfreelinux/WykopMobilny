package io.github.feelfreelinux.wykopmobilny.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.OpenableColumns
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.text.SpannableStringBuilder
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.ui.widgets.PreCachingLayoutManager
import io.github.feelfreelinux.wykopmobilny.utils.api.parseDate
import io.github.feelfreelinux.wykopmobilny.utils.api.parseDateJavaTime
import io.github.feelfreelinux.wykopmobilny.utils.recyclerview.ViewHolderDependentItemDecorator
import org.ocpsoft.prettytime.PrettyTime
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*


var View.isVisible : Boolean
    get() = visibility == View.VISIBLE
    set(value) { visibility = if (value) View.VISIBLE else View.GONE }

fun SpannableStringBuilder.appendNewSpan(text: CharSequence, what: Any, flags: Int): SpannableStringBuilder {
    val start = length
    append(text)
    setSpan(what, start, length, flags)
    return this
}

fun View.getActivityContext() : Activity? {
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun RecyclerView.prepare() {
    setItemViewCacheSize(20)
    drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    isDrawingCacheEnabled = true
    layoutManager = LinearLayoutManager(context)
    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    addItemDecoration(ViewHolderDependentItemDecorator(context))

}

fun RecyclerView.prepareNoDivider() {
    setItemViewCacheSize(20)
    drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    isDrawingCacheEnabled = true
    layoutManager = LinearLayoutManager(context)
}

fun View.disableFor(millis: Long){
    isEnabled = false
    postDelayed({
        isEnabled = true
    }, millis)
}

fun ImageView.loadImage(url : String, signature : Int? = null) {
    if (signature == null) {
        GlideApp.with(context)
                .load(url)
                .into(this)
    } else {
        GlideApp.with(context)
                .load(url)
                .apply(RequestOptions()
                        .signature(ObjectKey(signature)))
                .into(this)
    }
}

fun String.toPrettyDate() : String {
    return PrettyTime(Locale("pl")).format(parseDate(this))
}
fun String.toDurationPrettyDate() : String {
    val period = Period.between(parseDateJavaTime(this), LocalDate.now())
    if (period.years > 1 && period.months > 0) {
        return "${period.years} lata ${period.months} mies."
    } else if (period.years == 1 && period.months > 0) {
        return "${period.years} rok ${period.months} mies."
    } else if (period.years == 0 && period.months > 0) {
        return "${period.months} mies."
    } else if (period.months == 0 && period.days > 0) {
        return "${period.days} dni"
    } else {
        val durationTime = LocalTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss", Locale.GERMAN))
        val duration = Duration.between(durationTime, LocalTime.now())
        if (period.days == 0 && duration.toHours().toInt() > 0) {
            return "${duration.toHours().toInt()} godz."
        } else if (duration.toHours().toInt() == 0) {
            return "${duration.toMinutes().toInt()} min."
        }
    }
    return PrettyTime(Locale("pl")).formatDurationUnrounded(parseDate(this))
}
fun Uri.queryFileName(contentResolver: ContentResolver) : String {
    var result: String? = null
    if (scheme == "content") {
        val cursor = contentResolver.query(this, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor!!.close()
        }
    }
    if (result == null) {
        result = path
        val cut = result!!.lastIndexOf('/')
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}

fun Uri.getMimeType(contentResolver: ContentResolver): String {
    return if (scheme == ContentResolver.SCHEME_CONTENT) {
        contentResolver.getType(this)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(this
                .toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase())
    }
}

class KotlinGlideRequestListener(val failedListener : (GlideException?) -> Unit, val successListener : () -> Unit) : RequestListener<Drawable> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        failedListener(e)
        return false
    }

    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        successListener()
        return false
    }
}