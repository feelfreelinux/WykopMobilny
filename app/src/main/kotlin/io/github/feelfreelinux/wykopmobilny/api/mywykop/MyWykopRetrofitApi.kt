package io.github.feelfreelinux.wykopmobilny.api.mywykop

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.api.NotificationCountResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MyWykopRetrofitApi {
    @GET("/mywykop/NotificationsCount/appkey/$APP_KEY/{userkey}")
    fun getNotificationCount(@Path("userkey", encoded = true) userkey : String) : Call<NotificationCountResponse>

    @GET("/mywykop/HashTagsNotificationsCount/appkey/$APP_KEY/{userkey}")
    fun getHashTagsNotificationsCount(@Path("userkey", encoded = true) userkey : String) : Call<NotificationCountResponse>
}