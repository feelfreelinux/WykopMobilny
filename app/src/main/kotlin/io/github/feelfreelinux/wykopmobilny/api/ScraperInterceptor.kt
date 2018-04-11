package io.github.feelfreelinux.wykopmobilny.api

import android.webkit.CookieManager
import okhttp3.Interceptor
import okhttp3.Response

class ScraperInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val cookies = CookieManager.getInstance().getCookie("https://wykop.pl").split(";")
        cookies.forEach {
            builder.addHeader("Cookie", it)
        }

        return chain.proceed(builder.build())
    }
}