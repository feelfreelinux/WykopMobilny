package io.github.feelfreelinux.wykopmobilny

import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

fun sendPost(resource : String, params : ArrayList<Pair<String, String>>, secret : String, appkey : String, action : WykopApiManager.WykopApiAction) {
    val url = "https://a.wykop.pl/$resource/appkey/$appkey"

    var paramsStringToSign = ""
    params.forEachIndexed {
        i, obj -> paramsStringToSign += obj.second + ","
    }
    paramsStringToSign = paramsStringToSign.substring(0, (paramsStringToSign.length - 1))

    val md5sign = encryptMD5(secret+url+paramsStringToSign)

    url.httpPost(params).header(Pair("apisign", md5sign)).responseJson { request, response, result ->
        when(result) {
            is Result.Success ->
                action.success(result.get())
            is Result.Failure -> {
                // @TODO Handle failures
            }
        }
    }
}
fun sendGet(resource : String, params : String, secret : String, appkey : String, action : WykopApiManager.WykopApiAction) {
    val url = "https://a.wykop.pl/$resource/appkey/$appkey/$params"

    val md5sign = encryptMD5(secret+url)

    url.httpGet().header(Pair("apisign", md5sign)).responseJson { request, response, result ->
        when(result) {
            is Result.Success ->
                action.success(result.get())
            is Result.Failure -> {
                // @TODO Handle failures
            }
        }
    }
}
