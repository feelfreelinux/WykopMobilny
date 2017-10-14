package io.github.feelfreelinux.wykopmobilny.api.mywykop

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationCountResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MyWykopRetrofitApi {
    @GET("/mywykop/ReadNotifications/appkey/$APP_KEY")
    fun readNotifications() : Single<List<NotificationResponse>>

    @GET("/mywykop/Notifications/page/{page}/appkey/$APP_KEY")
    fun getNotifications(@Path("page") page : Int) : Single<List<NotificationResponse>>

    @GET("/mywykop/NotificationsCount/appkey/$APP_KEY")
    fun getNotificationCount() : Single<NotificationCountResponse>

    @GET("/mywykop/ReadHashTagsNotifications/appkey/$APP_KEY")
    fun readHashTagsNotifications() : Single<List<NotificationResponse>>

    @GET("/mywykop/HashTagsNotifications/page/{page}/appkey/$APP_KEY")
    fun getHashTagsNotifications(@Path("page") page : Int) : Single<List<NotificationResponse>>

    @GET("/mywykop/HashTagsNotificationsCount/appkey/$APP_KEY")
    fun getHashTagsNotificationsCount() : Single<NotificationCountResponse>
}