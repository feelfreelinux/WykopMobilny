package io.github.wykopmobilny.api.endpoints

import io.github.wykopmobilny.APP_KEY
import io.github.wykopmobilny.api.responses.NotificationResponse
import io.github.wykopmobilny.api.responses.NotificationsCountResponse
import io.github.wykopmobilny.api.responses.WykopApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationsRetrofitApi {

    @GET("/notifications/ReadDirectedNotifications/appkey/$APP_KEY")
    suspend fun readNotifications(): WykopApiResponse<List<NotificationResponse>>

    @GET("/notifications/Index/page/{page}/appkey/$APP_KEY")
    suspend fun getNotifications(@Path("page") page: Int): WykopApiResponse<List<NotificationResponse>>

    @GET("/notifications/Count/appkey/$APP_KEY")
    suspend fun getNotificationCount(): WykopApiResponse<NotificationsCountResponse>

    @GET("/notifications/ReadHashTagsNotifications/appkey/$APP_KEY")
    suspend fun readHashTagsNotifications(): WykopApiResponse<List<NotificationResponse>>

    @GET("/notifications/HashTags/page/{page}/appkey/$APP_KEY")
    suspend fun getHashTagsNotifications(@Path("page") page: Int): WykopApiResponse<List<NotificationResponse>>

    @GET("/notifications/HashTagsCount/appkey/$APP_KEY")
    suspend fun getHashTagsNotificationsCount(): WykopApiResponse<NotificationsCountResponse>
}
