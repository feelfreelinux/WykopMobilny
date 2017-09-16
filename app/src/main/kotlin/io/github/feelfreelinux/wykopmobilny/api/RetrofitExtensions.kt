package io.github.feelfreelinux.wykopmobilny.api

import io.github.feelfreelinux.wykopmobilny.utils.printout
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okio.Okio
import okio.BufferedSink
import okhttp3.RequestBody
import okhttp3.internal.Util
import okio.Source
import java.io.IOException
import java.io.InputStream


fun <T> Call<T>.enqueue(success: (response: Response<T>) -> Unit,
                                  failure: (t: Throwable) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) {
            success(response)
        }
        override fun onFailure(call: Call<T>?, t: Throwable) {
            printout("ASDASD")
            failure(t)
        }
    })
}

fun InputStream.getRequestBody(mimetype : String) =
    object : RequestBody() {
        override fun contentType(): MediaType? = MediaType.parse(mimetype)

        override fun contentLength(): Long {
            return try {
                available().toLong()
            } catch (e: IOException) {
                0
            }

        }

        override fun writeTo(sink: BufferedSink) {
            var source: Source? = null
            try {
                source = Okio.source(this@getRequestBody)
                sink.writeAll(source!!)
            } finally {
                Util.closeQuietly(source)
            }
        }
    }