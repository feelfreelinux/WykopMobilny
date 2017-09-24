package io.github.feelfreelinux.wykopmobilny.api.mywykop

import io.github.feelfreelinux.wykopmobilny.APP_KEY
import io.github.feelfreelinux.wykopmobilny.api.REQUIRES_LOGIN_HEADER
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationCountResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MyWykopRetrofitApi {
    @Headers("@: $REQUIRES_LOGIN_HEADER")
    @GET("/mywykop/NotificationsCount/appkey/$APP_KEY/{userkey}")
    fun getNotificationCount(@Path("userkey", encoded = true) userkey : String) : Single<NotificationCountResponse>

    @Headers("@: $REQUIRES_LOGIN_HEADER")
    @GET("/mywykop/HashTagsNotificationsCount/appkey/$APP_KEY/{userkey}")
    fun getHashTagsNotificationsCount(@Path("userkey", encoded = true) userkey : String) : Single<NotificationCountResponse>
}