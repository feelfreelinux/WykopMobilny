package io.github.feelfreelinux.wykopmobilny.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.OpenableColumns
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.parseDate
import org.ocpsoft.prettytime.PrettyTime
import java.util.*


var View.isVisible : Boolean
    get() = visibility == View.VISIBLE
    set(value) { visibility = if (value) View.VISIBLE else View.GONE }

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
    setHasFixedSize(true) // For better performance
    layoutManager = LinearLayoutManager(context)
}

fun View.disableFor(millis: Long){
    isEnabled = false
    postDelayed({
        isEnabled = true
    }, millis)
}

fun ImageView.loadImage(url : String) {
    GlideApp.with(context)
            .load(url)
            .transition(GenericTransitionOptions.with(R.anim.fade_in))
            .into(this)
}

fun String.toPrettyDate() : String = PrettyTime(Locale("pl")).format(parseDate(this))

fun Uri.queryFileName(contentResolver: ContentResolver) : String {
    val returnCursor = contentResolver.query(this, null, null, null, null)
    val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: return ""
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}

val CredentialsPreferencesApi.userSessionToken : String
    get() = if (userToken.isNullOrEmpty()) "" else "userkey/$userToken"

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