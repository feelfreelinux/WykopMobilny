package io.github.feelfreelinux.wykopmobilny.api

import io.github.feelfreelinux.wykopmobilny.APP_SECRET
import io.github.feelfreelinux.wykopmobilny.BuildConfig
import io.github.feelfreelinux.wykopmobilny.utils.api.encryptMD5
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import okio.Okio
import java.io.ByteArrayOutputStream
import java.util.ArrayList

const val REMOVE_USERKEY_HEADER = "REMOVE_USERKEY"

class ApiSignInterceptor(val userManagerApi: UserManagerApi) : Interceptor {

    companion object {
        const val MAX_RETRY_COUNT = 3
        const val API_SIGN_HEADER = "apisign"
    }

    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain!!.request()
        val builder = request.newBuilder()
        var url = request.url().toString()

        val customHeaders = request.headers("@")
        if (userManagerApi.isUserAuthorized() && !customHeaders.contains(REMOVE_USERKEY_HEADER))
            url += "/userkey/${userManagerApi.getUserCredentials()!!.userKey}"

        val encodeUrl: String = when (request.body()) {
            is FormBody -> {
                val formBody = request.body() as FormBody
                val paramList = (0 until formBody.size())
                    .filter { !formBody.value(it).isNullOrEmpty() }
                    .mapTo(ArrayList<String>()) { formBody.value(it) }
                    .toList()
                APP_SECRET + url + paramList.joinToString(",")
            }
            is MultipartBody -> {
                val multipart = request.body() as MultipartBody
                val parts = arrayListOf<String>()
                for (i in 0..1) {
                    val part = multipart.part(i).body()
                    // Get body from multipart
                    val bufferedSink = Okio.buffer(Okio.sink(ByteArrayOutputStream()))
                    part.writeTo(bufferedSink)
                    parts.add(bufferedSink.buffer().readUtf8())
                }
                APP_SECRET + url + parts.joinToString(",")
            }
            else -> APP_SECRET + url
        }
        if (BuildConfig.DEBUG) {
            printout(encodeUrl)
        }

        builder.apply {
            url(url)
            addHeader(API_SIGN_HEADER, encodeUrl.encryptMD5())
            removeHeader("@")
        }

        val newRequest = builder.build()

        var response = chain.proceed(newRequest)
        var tryCount = 0
        while (!response.isSuccessful && response.code() != 401 && tryCount < MAX_RETRY_COUNT) {
            tryCount++
            response = chain.proceed(newRequest)
        }
        return response
    }
}