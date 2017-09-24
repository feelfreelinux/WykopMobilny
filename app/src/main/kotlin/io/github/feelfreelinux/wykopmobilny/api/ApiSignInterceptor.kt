package io.github.feelfreelinux.wykopmobilny.api

import io.github.feelfreelinux.wykopmobilny.APP_SECRET
import io.github.feelfreelinux.wykopmobilny.utils.api.encryptMD5
import io.github.feelfreelinux.wykopmobilny.utils.printout
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import okio.Okio
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*

class ApiSignInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response? {
        val API_SIGN_HEADER = "apisign"
        val request = chain!!.request()
        val builder = request.newBuilder()

        val encodeUrl : String = when(request.body()) {
            is FormBody -> {
                val formBody = request.body() as FormBody
                val paramList = ArrayList<String>()

                (0 until formBody.size()).mapTo(paramList) { formBody.value(it) }
                paramList.sort()

                APP_SECRET + request.url().toString() + paramList.joinToString(",")
            }
            is MultipartBody -> {
                val multipart = request.body() as MultipartBody
                val part = multipart.part(0).body()

                // Get body from multipart
                val bufferedSink = Okio.buffer(Okio.sink(ByteArrayOutputStream()))
                part.writeTo(bufferedSink)
                val body = bufferedSink.buffer().readUtf8()

                APP_SECRET + request.url().toString() + body
            }
            else -> APP_SECRET + request.url().toString()
        }

        builder.addHeader(API_SIGN_HEADER, encodeUrl.encryptMD5())
        return chain.proceed(builder.build())
    }
}