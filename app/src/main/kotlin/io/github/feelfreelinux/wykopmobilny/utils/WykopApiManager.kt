package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import com.github.kittinunf.fuel.android.core.Json
import io.github.feelfreelinux.wykopmobilny.objects.APP_KEY
import io.github.feelfreelinux.wykopmobilny.objects.APP_SECRET
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import org.json.JSONArray
import org.json.JSONObject

class WykopApiManager(val context: Context) {

    var userToken = ""

    val apiPrefs = ApiPreferences()
    val login = apiPrefs.login!!
    val accountKey = apiPrefs.userKey!!

    lateinit var initAction: WykopApiAction
    var networkUtils: NetworkUtils = NetworkUtils()

    constructor(data: WykopApiData, context: Context) : this(context) {
        this.userToken = data.userToken as String
    }


    interface WykopApiAction {
        fun success(json: JSONObject) {}
        fun success(json: JSONArray) {}
    }

    fun getData(): WykopApiData = WykopApiData(login, accountKey, APP_SECRET, APP_KEY, apiPrefs.userToken)
    fun getUserSessionToken(successCallback: (JSONObject) -> Unit) {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("accountkey", accountKey))
        params.add(Pair("login", login))

        networkUtils.sendPost("user/login", params, getData(),
                object : WykopApiAction {
                    override fun success(json: JSONObject) {
                        apiPrefs.userToken = json.getString("userkey")
                        successCallback(json)
                    }
                }
        )
    }

    fun getEntry(id: Int, action: WykopApiAction) {
//        networkUtils.sendGet("entries/index/$id", "", getData(), action)
        networkUtils.getEntryDetails(id, successCallback = Unit, failureCallback = Unit)

    }

    fun getHot(page: Int, period: String, action: WykopApiAction) {
        networkUtils.sendGet("stream/hot", "page/$page/period/$period", getData(), action)
    }

    fun getNewestMikroblog(page: Int, action: WykopApiAction) {
        networkUtils.sendGet("stream/index", "page/$page", getData(), action)
    }

    fun getTagEntries(page: Int, tag: String, action: WykopApiAction) {
        networkUtils.sendGet("tag/entries/$tag", "page/$page", getData(), action)
    }

    fun entryVote(type: String, entryId: Int, commentId: Int?, vote: Boolean, action: WykopApiAction) {
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

    fun entryVote(entry: Entry, successCallback: (Int) -> Unit, failureCallback: () -> Unit) {
        if (entry.voted)
            networkUtils.unvoteForComment(data = getData(),
                    entryId = entry.id!!,
                    successCallback = successCallback,
                    failureCallback = failureCallback)
        else
            networkUtils.voteForComment(data = getData(),
                    entryId = entry.id!!,
                    successCallback = successCallback,
                    failureCallback = failureCallback)
    }
}