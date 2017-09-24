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