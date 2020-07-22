package io.github.feelfreelinux.wykopmobilny.ui.modules.photoview

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import java.io.File


interface PhotoViewCallbacks {
    fun shareImage(url: String)
    fun getDrawable(): Drawable?
    fun saveImage(url: String)
}

class PhotoViewActions(val context: Context) : PhotoViewCallbacks {

    companion object {
        const val SAVED_FOLDER = "wykopmobilny"
        const val SHARED_FOLDER = "udostępnione"
    }

    private val photoView = context as PhotoViewActivity

    override fun shareImage(url: String) {
        if (!checkForWriteReadPermission()) {
            return
        }

        Single.create(SingleOnSubscribe<File> {
            val file = Glide.with(context).downloadOnly().load(url).submit().get()
            var fileUrl = url.substringAfterLast("/")
            if (fileUrl.contains("?")) {
                fileUrl = fileUrl.substringBefore("?")
            }
            val newFile = File(file.path.substringBeforeLast("/") + "/" + fileUrl)
            file.renameTo(newFile)


            it.onSuccess(newFile)
        }).subscribeOn(WykopSchedulers().backgroundThread()).observeOn(WykopSchedulers().mainThread()).subscribe { file: File ->

            val url = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
            val share = Intent(Intent.ACTION_SEND)
            share.type = getMimeType(url.path!!)
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            share.putExtra(Intent.EXTRA_STREAM, url)
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            photoView.startActivityForResult(Intent.createChooser(share, "Udostępnij obrazek"), PhotoViewActivity.SHARE_REQUEST_CODE)
        }
    }

    override fun getDrawable(): Drawable? {
        /*photoView.image.drawable?.apply {
            return this
        }*/
        return null
    }

    override fun saveImage(url: String) {
        if (!checkForWriteReadPermission()) {
            return
        }
        Completable.fromAction {

            val relativeLocation = Environment.DIRECTORY_PICTURES
            var fileUrl = url.substringAfterLast("/")
            if (fileUrl.contains("?")) {
                fileUrl = fileUrl.substringBefore("?")
            }
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileUrl)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, getMimeType(url))
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation + "/" + PhotoViewActions.SAVED_FOLDER)
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            val uri = context.contentResolver.insert(contentUri, contentValues)
            uri?.let {
                uri ->
                val stream = context.contentResolver.openOutputStream(uri)

                val file = Glide.with(context).downloadOnly().load(url).submit().get()

                file.inputStream().copyTo(stream!!)

            }

        }.subscribeOn(WykopSchedulers().backgroundThread())
            .observeOn(WykopSchedulers().mainThread()).subscribe({
                showToastMessage("Zapisano plik")
            }, {
                showToastMessage("Błąd podczas zapisu pliku")
            })
    }

    private fun checkForWriteReadPermission(): Boolean {
        ActivityCompat.requestPermissions(
            photoView,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1
        )
        val writePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return writePermission != PackageManager.PERMISSION_DENIED && readPermission != PackageManager.PERMISSION_DENIED
    }

    private fun showToastMessage(text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

    private fun addImageToGallery(filePath: String, context: Context) {
        val values = ContentValues()

        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(Images.Media.MIME_TYPE, getMimeType(filePath))
        values.put(MediaStore.MediaColumns.DATA, filePath)

        context.contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun getMimeType(uri: String) =
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri))
}