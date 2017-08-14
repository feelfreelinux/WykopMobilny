package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import io.github.feelfreelinux.wykopmobilny.objects.*
import io.reactivex.Single

interface WykopApi {
    fun getUserSessionToken(): Single<Result<Profile, FuelError>>
    fun getTagEntries(page: Int, tag: String): Single<Result<TagFeedEntries, FuelError>>
    fun getNotificationCount(): Single<Result<NotificationCountResponse, FuelError>>
    fun getHashTagsNotificationsCount(): Single<Result<NotificationCountResponse, FuelError>>
    fun getMikroblogHot(page: Int, period: String): Single<Result<Array<Entry>, FuelError>>
    fun getMikroblogIndex(page: Int): Single<Result<Array<Entry>, FuelError>>
    fun getEntryIndex(id: Int): Single<Result<Entry, FuelError>>
    fun voteEntry(entryId: Int, commentId: Int?): Single<Result<VoteResponse, FuelError>>
    fun unvoteEntry(entryId: Int, commentId: Int?): Single<Result<VoteResponse, FuelError>>
}


class WykopApiManager(context: Context, val apiPrefs: ApiPreferences) : WykopApi {

    var networkUtils: NetworkUtils = NetworkUtils(context, apiPrefs)

    override fun getUserSessionToken(): Single<Result<Profile, FuelError>> {
        val params = ArrayList<Pair<String, String>>()
        params.add(Pair("accountkey", apiPrefs.userKey!!))
        params.add(Pair("login", apiPrefs.login!!))

        return networkUtils.sendPost<Profile>("user/login", null, params)
    }

    override fun getTagEntries(page: Int, tag: String): Single<Result<TagFeedEntries, FuelError>> =
            networkUtils.sendGet<TagFeedEntries>("tag/entries/$tag", "page/$page")

    override fun getNotificationCount(): Single<Result<NotificationCountResponse, FuelError>> =
            networkUtils.sendGet<NotificationCountResponse>("mywykop/NotificationsCount", null)

    override fun getHashTagsNotificationsCount(): Single<Result<NotificationCountResponse, FuelError>> =
            networkUtils.sendGet<NotificationCountResponse>("mywykop/HashTagsNotificationsCount", null)

    override fun getMikroblogHot(page: Int, period: String): Single<Result<Array<Entry>, FuelError>> =
            networkUtils.sendGet<Array<Entry>>("stream/hot", "page/$page/period/$period")

    override fun getMikroblogIndex(page: Int): Single<Result<Array<Entry>, FuelError>> =
            networkUtils.sendGet<Array<Entry>>("stream/index", "page/$page")

    override fun getEntryIndex(id: Int): Single<Result<Entry, FuelError>> =
            networkUtils.sendGet<Entry>("entries/index", "$id")


    override fun voteEntry(entryId: Int, commentId: Int?): Single<Result<VoteResponse, FuelError>> {
        val params = if (commentId == null) "entry/$entryId" else "comment/$entryId/$commentId"
        return networkUtils.sendGet<VoteResponse>("entries/vote", params)
    }

    override fun unvoteEntry(entryId: Int, commentId: Int?): Single<Result<VoteResponse, FuelError>> {
        val params = if (commentId == null) "entry/$entryId" else "comment/$entryId/$commentId"
        return networkUtils.sendGet<VoteResponse>("entries/unvote", params)
    }
}