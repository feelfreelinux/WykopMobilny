package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import com.github.kittinunf.fuel.android.core.Json
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import org.json.JSONArray
import org.json.JSONObject

class WykopApiManager(val login : String, val accountKey: String, val secret : String, val appkey : String, val context: Context) {
    var userToken = ""
    lateinit var initAction : WykopApiAction
    var networkUtils : NetworkUtils = NetworkUtils(context)

    constructor(data : WykopApiData, context: Context) : this(data.login, data.accountKey, data.secret, data.appkey, context) {
        this.userToken = data.userToken as String
    }


    interface WykopApiAction {
        fun success(json : JSONObject) {}
        fun success(json : JSONArray) {}
    }

    fun getData() : WykopApiData = WykopApiData(login, accountKey, secret, appkey, userToken)
    fun getUserSessionToken() {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("accountkey", accountKey))
        params.add(Pair("login", login))

        networkUtils.sendPost("user/login", params, getData(),
                object : WykopApiAction {
                    override fun success(json: JSONObject) {
                        userToken = json.getString("userkey")
                        initAction.success(json)
                    }
                }
        )
    }

    fun getEntry(id : Int, action : WykopApiAction) {
        networkUtils.sendGet("entries/index/$id", "", getData(), action)
    }

    fun getHot(page : Int, period : String, action : WykopApiAction) {
        networkUtils.sendGet("stream/hot", "page/$page/period/$period", getData(), action)
    }
    fun getNewestMikroblog(page : Int, action : WykopApiAction) {
        networkUtils.sendGet("stream/index", "page/$page", getData(), action)
    }

    fun getTagEntries(page : Int, tag : String, action : WykopApiAction) {
        networkUtils.sendGet("tag/entries/$tag", "page/$page", getData(), action)
    }


    fun voteEntry(type : String, entryId : Int, commentId : Int?, vote : Boolean, action : WykopApiAction) {
        if (commentId != null)
            if (vote)
                networkUtils.sendGet("entries/vote/$type/$entryId/$commentId", "", getData(), action)
            else
                networkUtils.sendGet("entries/unvote/$type/$entryId/$commentId", "", getData(), action)
        else
            if (vote)
                networkUtils.sendGet("entries/vote/$type/$entryId", "", getData(), action)
            else
                networkUtils.sendGet("entries/unvote/$type/$entryId", "", getData(), action)
    }
}