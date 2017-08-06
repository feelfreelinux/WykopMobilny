package io.github.feelfreelinux.wykopmobilny.utils

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.feelfreelinux.wykopmobilny.R
import org.json.JSONObject
import org.json.JSONTokener
import io.github.feelfreelinux.wykopmobilny.objects.*


class NetworkUtils(val context: Context, val apiPrefs: ApiPreferences) {

    fun sendGet(resource: String, params: String?, className: Class<*>, successCallback: (Any) -> Unit, failureCallback: () -> Unit) {
        val url =
                if (params == null) "https://a.wykop.pl/$resource/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"
                else "https://a.wykop.pl/$resource/$params/appkey/$APP_KEY/userkey/${apiPrefs.userToken}"

        val md5sign = "$APP_SECRET$url".encryptMD5()
        printout(url)

        url.httpGet().header(Pair("apisign", md5sign)).responseObject(Deserializer(className, context)) { _, _, result ->
            when (result) {
                is Result.Success ->
                    if (result.value != null) successCallback(result.value)
                is Result.Failure ->
                    failureCallback()
            }
        }
    }

    fun sendPost(resource: String, params: String?, postParams: ArrayList<Pair<String, String>>, className: Class<*>, successCallback: (Any) -> Unit, failureCallback: () -> Unit) {
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


        url.httpPost(postParams).header(Pair("apisign", md5sign)).responseObject(Deserializer(className, context)) { _, _, result ->
            when (result) {
                is Result.Success ->
                    if (result.value != null) successCallback(result.value)
                is Result.Failure ->
                    failureCallback()
            }
        }
    }

}

class Deserializer<T : Any>(val javaclassname: Class<T>, val context: Context) : ResponseDeserializable<T> {
    override fun deserialize(content: String) =
            if (checkResults(content)) Gson().fromJson(content, javaclassname)
            else null


    fun checkResults(json: String): Boolean {
        val jsonResult = JSONTokener(json).nextValue()
        printout(json)
        if (jsonResult is JSONObject && jsonResult.has("error")) {
            printout("DDD?")
            // Create alert
            val error = jsonResult.getJSONObject("error")
            val alertBuilder: AlertDialog.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1)
                alertBuilder = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
            else alertBuilder = AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
            alertBuilder.run {
                setTitle(context.getString(R.string.errorDialog))
                setMessage(error.getString("message") + " (${error.getInt("code")})")
                create().show()
            }
            return false
        } else
            return true
    }
}
