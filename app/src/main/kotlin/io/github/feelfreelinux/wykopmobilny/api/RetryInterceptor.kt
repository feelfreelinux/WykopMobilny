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

class RetryInterceptor() : Interceptor {
    companion object {
        val MAX_RETRY_COUNT = 3
    }
    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain!!.request()
        val builder = request.newBuilder()
        val url = request.url().toString()

        builder.apply {
            url(url)
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