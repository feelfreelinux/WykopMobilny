package io.github.feelfreelinux.wykopmobilny.utils

import android.app.AlertDialog
import android.content.Context
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import org.json.JSONObject
import org.json.JSONTokener

class NetworkUtils(val context: Context) {
    fun sendPost(resource: String, params: ArrayList<Pair<String, String>>, data: WykopApiData, action: WykopApiManager.WykopApiAction) {
        val url = "https://a.wykop.pl/$resource/appkey/${data.appkey}"

        var paramsStringToSign = ""
        params.forEachIndexed {
            _, obj ->
            paramsStringToSign += obj.second + ","
        }
        paramsStringToSign = paramsStringToSign.substring(0, (paramsStringToSign.length - 1))

        val md5sign = encryptMD5(data.secret + url + paramsStringToSign)

        url.httpPost(params).header(Pair("apisign", md5sign)).responseJson { _, _, result ->
            when (result) {
                is Result.Success -> {
                    val jsonResult = result.get()
                    if (checkResults(jsonResult)) action.success(jsonResult)
                }
                is Result.Failure -> {
                    // @TODO Handle failures
                }
            }
        }
    }

    fun sendGet(resource: String, params: String, data: WykopApiData, action: WykopApiManager.WykopApiAction) {
        val url = "https://a.wykop.pl/$resource/appkey/${data.appkey}/userkey/${data.userToken}/$params"

        val md5sign = encryptMD5(data.secret + url)
        printout(url)
        url.httpGet().header(Pair("apisign", md5sign)).responseJson { request, response, result ->
            when (result) {
                is Result.Success ->
                    try {
                        val jsonResult = result.get()
                        if(checkResults(jsonResult)) action.success(jsonResult)
                    } catch (e: Exception) {
                        printout("error " + e.message)
                    }
                is Result.Failure -> {
                    // @TODO Handle failures
                }
            }
        }
    }

    fun checkResults(jsonResult : Json) : Boolean {
        val json =  JSONTokener(jsonResult.content).nextValue()
        if (json is JSONObject && json.has("error")) {
            // Create alert
            val error = json.getJSONObject("error")
            var alertBuilder = AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert);
            alertBuilder.setTitle(context.getString(R.string.errorDialog))
            alertBuilder.setMessage(error.getString("message") + " (${error.getInt("code")})")
            alertBuilder.create().show()
            return false
        } else
            return true
    }
}
