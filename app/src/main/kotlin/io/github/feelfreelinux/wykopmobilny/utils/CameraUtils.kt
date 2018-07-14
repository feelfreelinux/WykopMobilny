package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.PhotoViewActions
import java.io.File
import java.io.IOException
import java.util.*

class CameraUtils {
    companion object {
        fun createPictureUri(context : Context): Uri? {
            val filename = String.format("owmcamera%d.jpg", Date().getTime())
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "${PhotoViewActions.SAVED_FOLDER}/${PhotoViewActions.SHARED_FOLDER}/" + filename)

            try {
                file.createNewFile()
            } catch (e: IOException) {
            }

            return FileProvider.getUriForFile(context, "io.github.feelfreelinux.wykopmobilny.fileprovider", file)
        }
    }
}