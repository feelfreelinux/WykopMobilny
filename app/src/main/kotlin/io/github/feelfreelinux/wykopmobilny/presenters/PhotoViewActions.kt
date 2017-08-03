package io.github.feelfreelinux.wykopmobilny.presenters

import android.Manifest
import android.content.Context
import android.provider.MediaStore.Images
import android.graphics.drawable.BitmapDrawable
import android.graphics.Bitmap
import android.net.Uri
import io.github.feelfreelinux.wykopmobilny.activities.PhotoViewActivity
import kotlinx.android.synthetic.main.activity_photoview.*
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream


class PhotoViewActions(val context : Context) {

    val photoView = context as PhotoViewActivity
    fun shareImage() {
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

    fun getImageBitmap() : Bitmap? = (photoView.image.drawable as BitmapDrawable).bitmap

    fun copyURL() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText("imageUrl", photoView.url)
        showToastMessage("Skopiowano do schowka")
    }

    fun saveImage() {
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

    fun checkForWriteReadPermission() : Boolean {
        val writePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        if (writePermission == PackageManager.PERMISSION_DENIED || readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(photoView, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            return false
        } else return true
    }

    fun showToastMessage(text : String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}