package io.github.feelfreelinux.wykopmobilny

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.android.core.Json

class WykopApiManager(val login : String, val accountKey: String, val secret : String, val appkey : String) {
    lateinit var userToken : String
    lateinit var initAction : WykopApiAction
    abstract class WykopApiAction {
        abstract fun success(json : Json)
    }

    init {
        getUserSessionToken()
    }

    fun getUserSessionToken() {
        var params = ArrayList<Pair<String, String>>()
        params.add(Pair("accountkey", accountKey))
        params.add(Pair("login", login))

        sendPost("user/login", params, secret, appkey,
                object : WykopApiAction() {
                    override fun success(json: Json) {
                        userToken = json.obj().getString("userkey")
                        initAction.success(json)
                    }
                }
        )
    }

    fun getHot(page : Int, period : String, action : WykopApiAction) {
        sendGet("stream/hot", "page/$page/period/$period", secret, appkey, action)
    }
}