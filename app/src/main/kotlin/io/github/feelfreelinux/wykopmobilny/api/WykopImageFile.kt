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
import java.io.File
import java.io.FileOutputStream

class WykopImageFile(val uri: Uri, val context: Context) {
    fun getFileMultipart(): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val filename = uri.queryFileName(contentResolver)
        var file: File? = null
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
            printout(opt.outMimeType)
            mimetype = opt.outMimeType

        }

        return MultipartBody.Part.createFormData("embed", filename, RequestBody.create(MediaType.parse(mimetype), file))
    }

    fun saveUri(uri: Uri, filename: String) : File? {
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
}