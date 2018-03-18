package io.github.feelfreelinux.wykopmobilny.api

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.utils.isConnectedToInternet
import okhttp3.Interceptor
import okhttp3.Response

class CacheControlInterceptor(val context: Context) : Interceptor {
    val IGNORE_CACHE = "IGNORE_CACHE_HEADER"
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val canCache = !request.headers("@").contains(IGNORE_CACHE)
        val originalResponse = chain.proceed(request.newBuilder().removeHeader("@").build())
        if (context.isConnectedToInternet() && canCache) {
            val maxAge = 60 // read from cache for 1 minute
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build()
        } else if (canCache) {
            val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build()
        } else {
            return originalResponse
        }
    }
}