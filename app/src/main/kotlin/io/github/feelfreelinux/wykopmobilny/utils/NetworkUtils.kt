package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.rx.rx_object
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import org.json.JSONObject
import org.json.JSONTokener
import io.github.feelfreelinux.wykopmobilny.objects.*
import io.reactivex.Single


class NetworkUtils(val context: Context, val apiPrefs: ApiPreferences) {
    inline fun <reified T : Any> sendGet(resource: String, params: String?): Single<Result<T, FuelError>> {
        val url =
                if (params == null) "https://a.wykop.pl/$resource/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"
                else "https://a.wykop.pl/$resource/$params/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"

        val md5sign = "$APP_SECRET$url".encryptMD5()
        printout(url)

        return url.httpGet().header(Pair("apisign", md5sign)).rx_object(ApiDeserializer(T::class.java))
    }

    inline fun <reified T : Any> sendPost(resource: String, params: String?, postParams: ArrayList<Pair<String, String>>): Single<Result<T, FuelError>> {
        val url =
                if (params == null) "https://a.wykop.pl/$resource/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"
                else "https://a.wykop.pl/$resource/$params/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"

        var paramsStringToSign = ""
        postParams.forEachIndexed {
            _, obj ->
            paramsStringToSign += obj.second + ","
        }
        paramsStringToSign = paramsStringToSign.substring(0, (paramsStringToSign.length - 1))

        val md5sign = "$APP_SECRET$url$paramsStringToSign".encryptMD5()

        return url.httpPost(postParams).header(Pair("apisign", md5sign)).rx_object(ApiDeserializer(T::class.java))
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