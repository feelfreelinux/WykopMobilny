package io.github.feelfreelinux.wykopmobilny.api

import com.github.kittinunf.fuel.core.Request
import io.github.feelfreelinux.wykopmobilny.utils.api.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.printout
import java.io.InputStream

interface WykopApi {
    fun getUserSessionToken(responseCallback: ApiResultCallback<Profile>): Request
    fun getTagEntries(page: Int, tag: String, responseCallback: ApiResultCallback<TagFeedEntries>): Request
    fun getNotificationCount(responseCallback: ApiResultCallback<NotificationCountResponse>): Request
    fun getHashTagsNotificationsCount(responseCallback: ApiResultCallback<NotificationCountResponse>): Request
    fun getMikroblogHot(page: Int, period: String, responseCallback: ApiResultCallback<Array<Entry>>): Request
    fun getMikroblogIndex(page: Int, responseCallback: ApiResultCallback<Array<Entry>>): Request
    fun getEntryIndex(id: Int, responseCallback: ApiResultCallback<Entry>): Request
    fun voteEntry(entryId: Int, commentId: Int?, responseCallback: ApiResultCallback<VoteResponse>): Request
    fun unvoteEntry(entryId: Int, commentId: Int?, responseCallback: ApiResultCallback<VoteResponse>): Request
    fun addNewEntry(body : String, embed : String?, responseCallback: ApiResultCallback<Any>): Request
    fun addNewEntryComment(entryId: Int, body: String, embed: String?, responseCallback: ApiResultCallback<Any>): Request
    fun addNewEntry(body: String, embed: Pair<String, InputStream>, responseCallback: ApiResultCallback<Any>): Request
    fun addNewEntryComment(entryId: Int, body: String, embed: Pair<String, InputStream>, responseCallback: ApiResultCallback<Any>): Request
}

class WykopApiManager(val apiPrefs: IApiPreferences) : WykopApi {

    var networkUtils: NetworkUtils = NetworkUtils(apiPrefs)

    override fun getUserSessionToken(responseCallback: ApiResultCallback<Profile>): Request {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("accountkey", apiPrefs.userKey!!))
        params.add(Pair("login", apiPrefs.login!!))
        return networkUtils.sendPost("user/login", null, params, responseCallback)
    }

    override fun getTagEntries(page: Int, tag: String, responseCallback: ApiResultCallback<TagFeedEntries>): Request =
            networkUtils.sendGet("tag/entries/$tag", "page/$page", responseCallback)

    override fun getNotificationCount(responseCallback: ApiResultCallback<NotificationCountResponse>): Request =
            networkUtils.sendGet("mywykop/NotificationsCount", null, responseCallback)

    override fun getHashTagsNotificationsCount(responseCallback: ApiResultCallback<NotificationCountResponse>): Request =
            networkUtils.sendGet("mywykop/HashTagsNotificationsCount", null, responseCallback)

    override fun getMikroblogHot(page: Int, period: String, responseCallback: ApiResultCallback<Array<Entry>>): Request =
            networkUtils.sendGet("stream/hot", "page/$page/period/$period", responseCallback)

    override fun getMikroblogIndex(page: Int, responseCallback: ApiResultCallback<Array<Entry>>): Request =
            networkUtils.sendGet("stream/index", "page/$page", responseCallback)

    override fun getEntryIndex(id: Int, responseCallback: ApiResultCallback<Entry>): Request =
            networkUtils.sendGet("entries/index", "$id", responseCallback)


    override fun voteEntry(entryId: Int, commentId: Int?, responseCallback: ApiResultCallback<VoteResponse>): Request {
        val params = if (commentId == null) "entry/$entryId" else "comment/$entryId/$commentId"
        return networkUtils.sendGet("entries/vote", params, responseCallback)
    }

    override fun unvoteEntry(entryId: Int, commentId: Int?, responseCallback: ApiResultCallback<VoteResponse>): Request {
        val params = if (commentId == null) "entry/$entryId" else "comment/$entryId/$commentId"
        return networkUtils.sendGet("entries/unvote", params, responseCallback)
    }

    override fun addNewEntry(body : String, embed : String?, responseCallback: ApiResultCallback<Any>): Request {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("body", body))
        embed?.let {
            params.add(Pair("embed", embed))
        }
        return networkUtils.sendPost("entries/add", null, params, responseCallback)
    }

    override fun addNewEntry(body : String, embed : Pair<String, InputStream>, responseCallback: ApiResultCallback<Any>): Request {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("body", body))
        return networkUtils.sendPostWithFile("entries/add", null, params, "embed", embed, responseCallback)
    }

    override fun addNewEntryComment(entryId: Int, body: String, embed: String?, responseCallback: ApiResultCallback<Any>): Request {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("body", body))
        embed?.let {
            params.add(Pair("embed", embed))
        }
        return networkUtils.sendPost("entries/addcomment", entryId.toString(), params, responseCallback)
    }

    override fun addNewEntryComment(entryId: Int, body: String, embed : Pair<String, InputStream>, responseCallback: ApiResultCallback<Any>): Request {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("body", body))
        return networkUtils.sendPostWithFile("entries/addcomment", entryId.toString(), params, "embed", embed, responseCallback)
    }
}