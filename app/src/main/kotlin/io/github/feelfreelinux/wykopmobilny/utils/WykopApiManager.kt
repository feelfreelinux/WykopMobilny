package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.objects.APP_KEY
import io.github.feelfreelinux.wykopmobilny.objects.APP_SECRET
import io.github.feelfreelinux.wykopmobilny.objects.User
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import org.json.JSONArray
import org.json.JSONObject

class WykopApiManager(context: Context) {

    var userToken = ""

    val apiPrefs = ApiPreferences()
    val login = apiPrefs.login!!
    var user : User? = null
    val accountKey = apiPrefs.userKey!!

    lateinit var initAction: WykopApiAction
    var networkUtils: NetworkUtils = NetworkUtils(context)

    constructor(data: WykopApiData, context: Context) : this(context) {
        this.userToken = data.userToken as String
        this.user = data.user
    }


    interface WykopApiAction {
        fun success(json: JSONObject) {}
        fun success(json: JSONArray) {}
    }

    fun getData(): WykopApiData = WykopApiData(user, accountKey, APP_SECRET, APP_KEY, userToken)
    fun getUserSessionToken(successCallback: (JSONObject) -> Unit) {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("accountkey", accountKey))
        params.add(Pair("login", login))

        networkUtils.sendPost("user/login", params, getData(),
                object : WykopApiAction {
                    override fun success(json: JSONObject) {
                        printout(json.toString())
                        userToken = json.getString("userkey")
                        user = parseUser(json)
                        successCallback(json)
                    }
                }
        )
    }

    fun getEntry(id: Int, action: WykopApiAction) {
        networkUtils.sendGet("entries/index/$id", "", getData(), action)
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

    fun getNotificationCount(action : WykopApiAction) {
        networkUtils.sendGet("mywykop/NotificationsCount", "", getData(), action)
    }

    fun getHashTagsNotificationsCount(action: WykopApiAction) {
        networkUtils.sendGet("mywykop/HashTagsNotificationsCount", "", getData(), action)
    }


    fun voteEntry(type: String, entryId: Int, commentId: Int?, vote: Boolean, action: WykopApiAction) {
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

//    fun entryVote(entry: Entry, successCallback: (Int) -> Unit, failureCallback: () -> Unit) {
//        if (entry.voted)
//            networkUtils.unvoteForComment(data = getData(),
//                    entryId = entry.id!!,
//                    successCallback = successCallback,
//                    failureCallback = failureCallback)
//        else
//            networkUtils.voteForComment(data = getData(),
//                    entryId = entry.id!!,
//                    successCallback = successCallback,
//                    failureCallback = failureCallback)
//    }
}