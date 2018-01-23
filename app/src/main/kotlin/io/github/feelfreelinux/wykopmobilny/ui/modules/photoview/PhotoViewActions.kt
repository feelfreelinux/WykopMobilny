package io.github.feelfreelinux.wykopmobilny.ui.modules.photoview

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore.Images
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.bumptech.glide.load.resource.gif.GifDrawable
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import kotlinx.android.synthetic.main.activity_photoview.*
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer


interface PhotoViewCallbacks {
    fun shareImage()
    fun getDrawable(): Drawable?
    fun saveImage()
}

class PhotoViewActions(val context : Context, clipboardHelperApi: ClipboardHelperApi) : PhotoViewCallbacks {
    val photoView = context as PhotoViewActivity
    override fun shareImage() {
        val drawable = getDrawable()
        if (drawable is BitmapDrawable) {
            if (checkForWriteReadPermission()) {
                val path = Images.Media.insertImage(context.contentResolver, drawable.bitmap, "title", null)
                val uri = Uri.parse(path)
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                context.startActivity(intent)
            }
        }
    }

    override fun getDrawable() : Drawable? {
        photoView.image.drawable?.apply {
            return this
        }
        return null
    }

    override fun saveImage() {
        val drawable = getDrawable()
        if (drawable == null || !checkForWriteReadPermission()) {
            return
        }
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "wykopmobilny")
            if (!file.exists()) file.mkdirs()

            val pictureFile = File("""${file.absoluteFile}/${photoView.url.substringAfterLast('/')}""")
            val fos = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
            showToastMessage("Zapisano obraz")
        }
        else if (drawable is GifDrawable) {
            val byteBuffer = drawable.buffer

            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "wykopmobilny")
            if (!file.exists()) file.mkdirs()
            val gifFile = File("""${file.absoluteFile}/${photoView.url.substringAfterLast('/')}""")

            val output = FileOutputStream(gifFile)
            val bytes = ByteArray(byteBuffer.capacity())
            (byteBuffer.duplicate().clear() as ByteBuffer).get(bytes)
            output.write(bytes, 0, bytes.size)
            output.close()
            showToastMessage("Zapisano GIFa")
        }
    }

    private fun checkForWriteReadPermission() : Boolean {
        val writePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        return if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(photoView, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            false
        } else true
    }

    private fun showToastMessage(text : String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}