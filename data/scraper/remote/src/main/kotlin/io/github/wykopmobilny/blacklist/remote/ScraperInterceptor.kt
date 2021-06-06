package io.github.wykopmobilny.blacklist.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class ScraperInterceptor @Inject constructor(
    private val baseUrl: String,
    private val cookiesProvider: (String) -> String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        cookiesProvider(baseUrl)
            .split(";")
            .forEach { builder.addHeader("Cookie", it) }

        return chain.proceed(builder.build())
    }
}
