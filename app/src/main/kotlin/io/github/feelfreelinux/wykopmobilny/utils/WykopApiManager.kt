package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.objects.*

class WykopApiManager(context: Context) {
    val apiPrefs = ApiPreferences()
    val login = apiPrefs.login!!
    val accountKey = apiPrefs.userKey!!
    val networkUtils by lazy { NetworkUtils(context) }

    fun getUserSessionToken(successCallback: ApiResponseCallback) {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("accountkey", accountKey))
        params.add(Pair("login", login))

        networkUtils.sendPost("user/login", null, params, Profile::class.java, successCallback, {})
    }

    fun getTagEntries(page: Int, tag: String, successCallback: ApiResponseCallback) =
        networkUtils.sendGet("tag/entries/$tag", "page/$page", TagFeedEntries::class.java, successCallback, {})

    fun getNotificationCount(successCallback: ApiResponseCallback) =
        networkUtils.sendGet("mywykop/NotificationsCount", null, NotificationCountResponse::class.java, successCallback, {})

    fun getHashTagsNotificationsCount(successCallback: ApiResponseCallback) =
        networkUtils.sendGet("mywykop/HashTagsNotificationsCount", null, NotificationCountResponse::class.java, successCallback, {})

    fun getMikroblogHot(page : Int, period : String, successCallback: ApiResponseCallback) =
        networkUtils.sendGet("stream/hot", "page/$page/period/$period", Array<Entry>::class.java, successCallback, {})

    fun getMikroblogIndex(page : Int, successCallback: ApiResponseCallback) =
        networkUtils.sendGet("stream/index", "page/$page", Array<Entry>::class.java, successCallback, {})

    fun getEntryIndex(id : Int, successCallback: ApiResponseCallback) =
        networkUtils.sendGet("entries/index", "$id", Entry::class.java,  successCallback, {})

    fun voteEntry(entryId : Int, commentId : Int?, successCallback: ApiResponseCallback) {
        val params = if (commentId == null) "entry/$entryId" else "comment/$entryId/$commentId"
        networkUtils.sendGet("entries/vote", params, VoteResponse::class.java, successCallback, {})
    }

    fun unvoteEntry(entryId : Int, commentId : Int?, successCallback: ApiResponseCallback) {
        val params = if (commentId == null) "entry/$entryId" else "comment/$entryId/$commentId"
        networkUtils.sendGet("entries/unvote", params, VoteResponse::class.java, successCallback, {})
    }
}