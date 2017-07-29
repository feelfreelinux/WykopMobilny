package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.objects.*
import org.json.JSONArray
import org.json.JSONObject

class WykopApiManager(context: Context) {

    var userToken = ""

    val apiPrefs = ApiPreferences()
    val login = apiPrefs.login!!
    var user : User? = null
    val accountKey = apiPrefs.userKey!!
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

    fun getTagEntries(page: Int, tag: String, successCallback: (Any) -> Unit) {
        networkUtils.sendGetNew("tag/entries/$tag", "page/$page", TagFeedEntries::class.java, getData(), successCallback, {})
    }

    fun getNotificationCount(action : WykopApiAction) {
        networkUtils.sendGet("mywykop/NotificationsCount", "", getData(), action)
    }

    fun getHashTagsNotificationsCount(action: WykopApiAction) {
        networkUtils.sendGet("mywykop/HashTagsNotificationsCount", "", getData(), action)
    }

    fun getMikroblogHot(page : Int, period : String, successCallback: (Any) -> Unit) {
        networkUtils.sendGetNew("stream/hot", "page/$page/period/$period", Array<SingleEntry>::class.java, getData(), successCallback, {})
    }

    fun getEntryIndex(id : Int, successCallback: (Any) -> Unit) {
        networkUtils.sendGetNew("entries/index", "$id", EntryDetails::class.java, getData(), successCallback, {})
    }
}