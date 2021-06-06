package io.github.wykopmobilny.api

import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor : Interceptor {

    companion object {
        private const val MAX_RETRY_COUNT = 3
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val url = request.url.toString()

        builder.apply {
            url(url)
            removeHeader("@")
        }

        val newRequest = builder.build()

        var response = chain.proceed(newRequest)
        var tryCount = 0
        while (!response.isSuccessful && response.code != 401 && tryCount < MAX_RETRY_COUNT) {
            tryCount++
            response.close()
            response = chain.proceed(newRequest)
        }
        response.close()
        return response
    }
}
