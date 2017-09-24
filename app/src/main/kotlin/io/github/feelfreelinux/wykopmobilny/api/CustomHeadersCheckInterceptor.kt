package io.github.feelfreelinux.wykopmobilny.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class WykopUserNotLoggedInException : IOException()

const val REQUIRES_LOGIN_HEADER = "RequiresLogin"
class CustomHeadersCheckInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val customHeaders = request.headers().values("@")
        if (customHeaders.contains(REQUIRES_LOGIN_HEADER)) {
            if (!request.url().toString().contains("userkey")) {
                throw WykopUserNotLoggedInException()
            }
        }
        return chain.proceed(
                request.newBuilder().removeHeader("@").build()
        )
    }
}