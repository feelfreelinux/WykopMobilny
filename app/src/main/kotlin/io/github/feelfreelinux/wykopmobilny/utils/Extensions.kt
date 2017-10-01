package io.github.feelfreelinux.wykopmobilny.utils

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.ui.photoview.launchPhotoView
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.parseDate
import org.ocpsoft.prettytime.PrettyTime
import java.util.*


var View.isVisible : Boolean
    get() = visibility == View.VISIBLE
    set(value) { visibility = if (value) View.VISIBLE else View.GONE }

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

fun ImageView.setPhotoViewUrl( url : String) {
    setOnClickListener {
        context.run {
            launchPhotoView(url)
        }
    }
}

fun String.toPrettyDate() : String = PrettyTime(Locale("pl")).format(parseDate(this))

fun Uri.queryFileName(contentResolver: ContentResolver) : String {
    val returnCursor = contentResolver.query(this, null, null, null, null)!!
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
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