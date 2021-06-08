package io.github.wykopmobilny.wykop.remote

import io.github.wykopmobilny.APP_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class AppKeyReplacingInterceptor @Inject constructor(
    @AppKey private val appKey: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val index = originalUrl.pathSegments.indexOf(APP_KEY)
        val newUrl = if (index >= 0) {
            originalUrl.newBuilder().setPathSegment(index, appKey).build()
        } else {
            originalUrl
        }

        val request = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request = request)
    }
}
