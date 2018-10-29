package io.github.feelfreelinux.wykopmobilny.api

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import io.github.feelfreelinux.wykopmobilny.utils.FileUtils
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.queryFileName
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.google.android.youtube.player.internal.y
import com.google.android.youtube.player.internal.x
import android.graphics.Bitmap
import android.R.attr.orientation
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import com.evernote.android.job.JobProxy.Common
import com.google.android.youtube.player.internal.v
import io.github.feelfreelinux.wykopmobilny.R.id.imageView
import java.io.*
import android.os.Environment.getExternalStorageDirectory




class WykopImageFile(val uri: Uri, val context: Context) {

    fun getFileMultipart(): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val filename = uri.queryFileName(contentResolver)
        var file: File?
        try {
            file = FileUtils.getFile(context, uri)
            if (file == null) {
                file = saveUri(uri, filename)
            }
        } catch (e: Throwable) {
            file = saveUri(uri, filename)
        }

        var mimetype = contentResolver.getType(uri)

        file?.let {
            val opt = BitmapFactory.Options()
            opt.inJustDecodeBounds = true
            BitmapFactory.decodeFile(it.absolutePath, opt)
            mimetype = opt.outMimeType

        }

        val rotatedFile = ensureRotation(file)
        printout(rotatedFile!!.name!!)
        return MultipartBody.Part.createFormData("embed", rotatedFile!!.name, RequestBody.create(MediaType.parse(mimetype), rotatedFile))
    }

    private fun saveUri(uri: Uri, filename: String): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream.use { input ->
            val file = File.createTempFile(filename, "0", context.cacheDir)
            val output = FileOutputStream(file)
            try {
                val buffer = ByteArray(4 * 1024) // or other buffer size

                var read = input.read(buffer)
                while (read != -1) {
                    output.write(buffer, 0, read)
                    read = input.read(buffer)
                }

                output.flush()
            } finally {
                output.close()
                return file
            }
        }
        return null
    }

    private fun ensureRotation(f: File?): File? {
        try {
            val exif = ExifInterface(f!!.getPath())
            val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL)

            var angle = 0

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270
            }
            if (angle == 0) return f

            val mat = Matrix()
            mat.postRotate(angle.toFloat())
            val options = BitmapFactory.Options()
            options.inSampleSize = 2

            val bmp = BitmapFactory.decodeStream(FileInputStream(f),
                    null, options)
            val bitmap = Bitmap.createBitmap(bmp!!, 0, 0, bmp.width,
                    bmp.height, mat, true)

            val file = File.createTempFile("rSaved", ".0", context.cacheDir)
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
            return file

        } catch (e: IOException) {
            Log.w("TAG", "-- Error in setting image")
        } catch (oom: OutOfMemoryError) {
            Log.w("TAG", "-- OOM Error in setting image")
        }
        return f
    }
}