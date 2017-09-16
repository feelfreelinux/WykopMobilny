package io.github.feelfreelinux.wykopmobilny.ui.photoview

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore.Images
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_photoview.*
import java.io.File
import java.io.FileOutputStream


interface PhotoViewCallbacks {
    fun shareImage()
    fun getImageBitmap(): Bitmap?
    fun copyURL()
    fun saveImage()
}

class PhotoViewActions(val context : Context) : PhotoViewCallbacks {
    val photoView = context as PhotoViewActivity
    override fun shareImage() {
        val bitmap = getImageBitmap()
        if ( bitmap != null && checkForWriteReadPermission()) {
            val path = Images.Media.insertImage(context.contentResolver, bitmap, "title", null)
            val uri = Uri.parse(path)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(intent)
        }
    }

    override fun getImageBitmap() : Bitmap? = (photoView.image.drawable as BitmapDrawable).bitmap

    override fun copyURL() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText("imageUrl", photoView.url)
        showToastMessage("Skopiowano do schowka")
    }

    override fun saveImage() {
        val bitmap = getImageBitmap()
        if (bitmap != null && checkForWriteReadPermission()) {
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "OtwartyWykopMobilny")
            if (!file.exists()) file.mkdirs()

            val pictureFile = File("""${file.absoluteFile}/${photoView.url.substringAfterLast('/')}""")
            val fos = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
            showToastMessage("Zapisano obraz")
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