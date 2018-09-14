package io.github.feelfreelinux.wykopmobilny.api.notifications

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationsCountResponse
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