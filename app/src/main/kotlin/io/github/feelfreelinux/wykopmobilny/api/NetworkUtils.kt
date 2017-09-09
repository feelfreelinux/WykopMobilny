package io.github.feelfreelinux.wykopmobilny.api

import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.core.interceptors.cUrlLoggingRequestInterceptor
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.APP_SECRET
import org.json.JSONObject
import org.json.JSONTokener
import io.github.feelfreelinux.wykopmobilny.utils.api.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.api.encryptMD5
import io.github.feelfreelinux.wykopmobilny.utils.printout
import java.io.InputStream

typealias ApiResultCallback<T> = (Result<T, FuelError>) -> Unit

const val BASE_URL = "https://a.wykop.pl"
class NetworkUtils(val apiPrefs: IApiPreferences) {
    private fun ApiSignInterceptor() =
            { next: (Request) -> Request ->
                { r: Request ->
                    // Sign the request
                    var paramsStringToSign = ""
                    if (!r.parameters.isEmpty()) {
                        r.parameters.forEach { paramsStringToSign += it.second as String + "," }
                        paramsStringToSign = paramsStringToSign.substring(0, (paramsStringToSign.length - 1))
                    }
                    val md5 = (APP_SECRET + r.url + paramsStringToSign).encryptMD5()
                    r.header(Pair("apisign", md5))
                    next(r)
                }
            }

    val manager by lazy { FuelManager() }
    init {
        // Setup FuelManager
        manager.basePath = BASE_URL
        manager.addRequestInterceptor(ApiSignInterceptor())
    }

    inline fun <reified T : Any> sendGet(resource: String, params: String?, crossinline resultCallback : ApiResultCallback<T>) : Request  {
        val url =
                if (params == null) "/$resource/appkey/${APP_KEY}/userkey/${apiPrefs.userToken}"
                else "/$resource/$params/appkey/${APP_KEY}/userkey/${apiPrefs.userToken}"

        return manager.request(Method.GET, url).responseObject(ApiDeserializer(T::class.java)) {
            _, _, result -> resultCallback.invoke(result)
        }
    }

    inline fun <reified T : Any> sendPost(resource: String, params: String?, postParams: ArrayList<Pair<String, String>>, crossinline resultCallback : ApiResultCallback<T>): Request {
        val url =
                if (params == null) "/$resource/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"
                else "/$resource/$params/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"

        return manager.request(Method.POST, url, postParams).responseObject(ApiDeserializer(T::class.java)) {
            _, _, result -> resultCallback.invoke(result)
        }
    }

    inline fun <reified T : Any> sendPostWithFile(resource: String, params: String?, postParams: ArrayList<Pair<String, String>>, fileParam : String, file : Pair<String, InputStream>, crossinline resultCallback : ApiResultCallback<T>): Request {
        val url =
                if (params == null) "/$resource/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"
                else "/$resource/$params/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"

        return manager.upload(url, Method.POST, postParams)
                .blob { _, _ ->
                    Blob(file.first, file.second.available().toLong(), { file.second })
                }
                .name { fileParam }
                .responseObject(ApiDeserializer(T::class.java)) {
                    _, _, result -> resultCallback.invoke(result)
                }
    }

    class ApiDeserializer<T : Any>(val javaclassname: Class<T>) : ResponseDeserializable<T> {
        override fun deserialize(content: String): T? {
            // Check for WykopApi Errors
            val jsonResult = JSONTokener(content).nextValue()
            if (jsonResult is JSONObject && jsonResult.has("error")) {
                val error = jsonResult.getJSONObject("error")
                val errorCode = error.getInt("code")
                val errorMessage = error.getString("message")

                throw WykopApiError(errorCode, errorMessage)
            } else return Gson().fromJson(content, javaclassname)
        }

    }

    class WykopApiError(val errorCode : Int, val errorMessage : String) : Exception() {
        override val message: String?
            get() = "$errorMessage ($errorCode)"

    }
}