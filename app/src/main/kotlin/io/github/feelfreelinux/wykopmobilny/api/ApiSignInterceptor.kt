package io.github.feelfreelinux.wykopmobilny.api

import io.github.feelfreelinux.wykopmobilny.APP_SECRET
import io.github.feelfreelinux.wykopmobilny.utils.api.encryptMD5
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import okio.Okio
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*

const val REMOVE_USERKEY_HEADER = "REMOVE_USERKEY"
class ApiSignInterceptor(val userManagerApi: UserManagerApi) : Interceptor {
    override fun intercept(chain: Interceptor.Chain?): Response? {
        val API_SIGN_HEADER = "apisign"
        val request = chain!!.request()
        val builder = request.newBuilder()
        var url = request.url().toString()

        val customHeaders = request.headers("@")
        if (userManagerApi.isUserAuthorized() && !customHeaders.contains(REMOVE_USERKEY_HEADER))
            url += "/userkey/${userManagerApi.getUserCredentials()!!.userKey}"

        val encodeUrl : String = when(request.body()) {
            is FormBody -> {
                val formBody = request.body() as FormBody
                var paramList = (0 until formBody.size())
                        .filter { !formBody.value(it).isNullOrEmpty() }
                        .sortedWith(compareBy({ formBody.name(it) }))
                        .mapTo(ArrayList<String>()) { formBody.value(it) }
                        .toList()
                if (customHeaders.contains(REMOVE_USERKEY_HEADER)) paramList = paramList.reversed()

                APP_SECRET + url+ paramList.joinToString(",")
            }
            is MultipartBody -> {
                val multipart = request.body() as MultipartBody
                val part = multipart.part(0).body()

                // Get body from multipart
                val bufferedSink = Okio.buffer(Okio.sink(ByteArrayOutputStream()))
                part.writeTo(bufferedSink)
                val text_body = bufferedSink.buffer().readUtf8()

                APP_SECRET + url + text_body
            }
            else -> APP_SECRET + url
        }
        printout(encodeUrl)

        builder.apply {
            url(url)
            addHeader(API_SIGN_HEADER, encodeUrl.encryptMD5())
            removeHeader("@")
        }
        return chain.proceed(builder.build())
    }
}