package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.WykopApiResponse
import io.github.wykopmobilny.api.responses.NotificationResponse
import io.github.wykopmobilny.api.responses.NotificationsCountResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationsRetrofitApi {
    @GET("/notifications/ReadDirectedNotifications/appkey/$APP_KEY")
    fun readNotifications(): Single<WykopApiResponse<List<NotificationResponse>>>

    @GET("/notifications/Index/page/{page}/appkey/$APP_KEY")
    fun getNotifications(@Path("page") page: Int): Single<WykopApiResponse<List<NotificationResponse>>>

    @GET("/notifications/Count/appkey/$APP_KEY")
    fun getNotificationCount(): Single<WykopApiResponse<NotificationsCountResponse>>

    @GET("/notifications/ReadHashTagsNotifications/appkey/$APP_KEY")
    fun readHashTagsNotifications(): Single<WykopApiResponse<List<NotificationResponse>>>

    @GET("/notifications/HashTags/page/{page}/appkey/$APP_KEY")
    fun getHashTagsNotifications(@Path("page") page: Int): Single<WykopApiResponse<List<NotificationResponse>>>

    @GET("/notifications/HashTagsCount/appkey/$APP_KEY")
    fun getHashTagsNotificationsCount(): Single<WykopApiResponse<NotificationsCountResponse>>
}
