package io.github.feelfreelinux.wykopmobilny.utils

import android.content.ContentResolver
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.instance
import io.github.feelfreelinux.wykopmobilny.utils.api.parseDate
import io.github.feelfreelinux.wykopmobilny.ui.photoview.launchPhotoView
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns





fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

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

fun Uri.getFullPath(contentResolver: ContentResolver): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(this, projection, null, null, null)
    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val path = cursor.getString(column_index)
    cursor.close()
    return path
}

fun Uri.queryFileName(contentResolver: ContentResolver) : String {
    val returnCursor = contentResolver.query(this, null, null, null, null)!!
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}