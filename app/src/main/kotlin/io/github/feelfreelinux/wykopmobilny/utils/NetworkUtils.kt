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
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.VoteResponse
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import org.json.JSONArray
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
                    val jsonObj =  JSONTokener(jsonResult.content).nextValue()
                    if(checkResults(jsonObj)) {
                        if (jsonObj is JSONObject)
                            action.success(jsonObj)
                        else if (jsonObj is JSONArray)
                            action.success(jsonObj)
                    }
                }
                is Result.Failure -> {
                    printout(result.error.toString())
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
                        val jsonObj =  JSONTokener(jsonResult.content).nextValue()
                        if(checkResults(jsonObj)) {
                            if (jsonObj is JSONObject)
                                action.success(jsonObj)
                            else if (jsonObj is JSONArray)
                                action.success(jsonObj)
                        }
                    } catch (e: Exception) {
                        printout("error " + e.message)
                    }
                is Result.Failure -> {
                    // @TODO Handle failures
                }
            }
        }
    }

    fun checkResults(jsonResult : Any) : Boolean {
        if (jsonResult is JSONObject && jsonResult.has("error")) {
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

    fun voteForComment(data: WykopApiData, entryId: Int, successCallback: (Int) -> Unit, failureCallback: () -> Unit) {
        val url = "https://a.wykop.pl/entries/vote/entry/$entryId/appkey/${data.appkey}/userkey/${data.userToken}/"

        val md5sign = encryptMD5(data.secret + url)
        printout(url)
        printout(md5sign)
        url.httpGet().header(Pair("apisign", md5sign)).responseObject(Deserializer(VoteResponse::class.java)) { request, response, result ->
            when (result) {
                is Result.Success -> {
                    successCallback(result.value.voters.size) }
                is Result.Failure -> {
                    failureCallback()
                }
            }
        }
    }

    fun unvoteForComment(data: WykopApiData, entryId: Int, successCallback: (Int) -> Unit, failureCallback: () -> Unit) {
        val url = "https://a.wykop.pl/entries/unvote/entry/$entryId/appkey/${data.appkey}/userkey/${data.userToken}/"

        val md5sign = encryptMD5(data.secret + url)
        printout(url)
        url.httpGet().header(Pair("apisign", md5sign)).responseObject(Deserializer(VoteResponse::class.java)) { request, response, result ->
            when (result) {
                is Result.Success -> {
                    successCallback(result.value.voters.size) }
                is Result.Failure -> {
                    failureCallback()
                }
            }
        }

    }
}

class Deserializer<T : Any> (val javaclassname: Class<T>) : ResponseDeserializable<T> {
    override fun deserialize(content: String) = Gson().fromJson(content, javaclassname)
}
