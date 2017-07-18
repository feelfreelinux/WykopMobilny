package io.github.feelfreelinux.wykopmobilny

import android.content.Context
import com.github.kittinunf.fuel.android.core.Json
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import java.io.Serializable

class WykopApiManager(val login : String, val accountKey: String, val secret : String, val appkey : String) {
    lateinit var userToken : String
    lateinit var initAction : WykopApiAction

    constructor(data : WykopApiData) : this(data.login, data.accountKey, data.secret, data.appkey) {
        this.userToken = data.userToken
    }

    abstract class WykopApiAction {
        abstract fun success(json : Json)
    }

    fun getData() : WykopApiData = WykopApiData(login, accountKey, secret, appkey, userToken)
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

    fun getEntry(id : Int, action : WykopApiAction) {
        sendGet("entries/index/$id", "", secret, appkey, action)
    }

    fun getHot(page : Int, period : String, action : WykopApiAction) {
        sendGet("stream/hot", "page/$page/period/$period", secret, appkey, action)
    }
    fun getNewestMikroblog(page : Int, action : WykopApiAction) {
        sendGet("stream/index", "page/$page", secret, appkey, action)
    }
}