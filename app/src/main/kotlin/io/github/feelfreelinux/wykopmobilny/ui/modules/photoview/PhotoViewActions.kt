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
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import io.github.feelfreelinux.wykopmobilny.base.WykopSchedulers
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import kotlinx.android.synthetic.main.activity_photoview.*
import java.io.File




interface PhotoViewCallbacks {
    fun shareImage(url: String)
    fun getDrawable(): Drawable?
    fun saveImage(url: String)
}

class PhotoViewActions(val context : Context, clipboardHelperApi: ClipboardHelperApi) : PhotoViewCallbacks {
    val photoView = context as PhotoViewActivity

    override fun shareImage(url: String) {
        if (!checkForWriteReadPermission()) {
            return
        }

        Single.create(SingleOnSubscribe<File>{
            val file = Glide.with(context).downloadOnly().load(url).submit().get()
            val newFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "wykopmobilny/udostępnione/" + url.substringAfterLast("/"))
            file.copyTo(newFile, true)
            it.onSuccess(newFile)
        }).subscribeOn(WykopSchedulers().backgroundThread()).observeOn(WykopSchedulers().mainThread()).subscribe { file: File ->
            addImageToGallery(file.path, context)
            val url = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
            val share = Intent(Intent.ACTION_SEND)
            share.type = getMimeType(url.path)
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            share.putExtra(Intent.EXTRA_STREAM, url)
            photoView.startActivityForResult(Intent.createChooser(share, "Udostępnij obrazek"), PhotoViewActivity.SHARE_REQUEST_CODE)
        }
    }

    override fun getDrawable() : Drawable? {
        photoView.image.drawable?.apply {
            return this
        }
        return null
    }

    override fun saveImage(url: String) {
        if (!checkForWriteReadPermission()) {
            return
        }
        Completable.fromAction({
            val file = Glide.with(context).downloadOnly().load(url).submit().get()
            var path = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "wykopmobilny")
            path = File(path, photoView.url.substringAfterLast('/'))
            file.copyTo(path, true)
            addImageToGallery(path.path, context)
        }).subscribeOn(WykopSchedulers().backgroundThread())
                .observeOn(WykopSchedulers().mainThread()).subscribe({
                    showToastMessage("Zapisano plik")
                }, {
                    showToastMessage("Błąd podczas zapisu pliku")
                })
    }

    private fun checkForWriteReadPermission() : Boolean {
        ActivityCompat.requestPermissions(photoView,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        val writePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        return writePermission != PackageManager.PERMISSION_DENIED && readPermission != PackageManager.PERMISSION_DENIED
    }

    private fun showToastMessage(text : String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun addImageToGallery(filePath: String, context: Context) {
        val values = ContentValues()

        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(Images.Media.MIME_TYPE, getMimeType(filePath))
        values.put(MediaStore.MediaColumns.DATA, filePath)

        context.contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    private fun getMimeType(uri: String): String {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri))
    }
}