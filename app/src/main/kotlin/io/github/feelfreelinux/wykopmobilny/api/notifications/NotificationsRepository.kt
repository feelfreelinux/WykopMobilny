package io.github.feelfreelinux.wykopmobilny.api.notifications

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.NotificationMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationsCountResponse
import retrofit2.Retrofit

class NotificationsRepository(val retrofit: Retrofit, val userTokenRefresher: UserTokenRefresher) : NotificationsApi {
    private val mywykopApi by lazy { retrofit.create(NotificationsRetrofitApi::class.java) }

    override fun readNotifications() = mywykopApi.readNotifications()
            .retryWhen(userTokenRefresher)
            .map { emptyList<Notification>() }

    override fun getNotifications(page : Int) = mywykopApi.getNotifications(page)
            .retryWhen(userTokenRefresher)
            .compose<List<NotificationResponse>>(ErrorHandlerTransformer())
            .map { it.map { NotificationMapper.map(it) } }

    override fun getNotificationCount() = mywykopApi.getNotificationCount()
            .retryWhen(userTokenRefresher)
            .compose<NotificationsCountResponse>(ErrorHandlerTransformer())

    override fun readHashTagNotifications() = mywykopApi.readHashTagsNotifications()
            .retryWhen(userTokenRefresher)
            .compose<List<NotificationResponse>>(ErrorHandlerTransformer())
            .map { it.map { NotificationMapper.map(it) } }

    override fun getHashTagNotifications(page : Int) = mywykopApi.getHashTagsNotifications(page)
            .retryWhen(userTokenRefresher)
            .compose<List<NotificationResponse>>(ErrorHandlerTransformer())
            .map { it.map { NotificationMapper.map(it) } }

    override fun getHashTagNotificationCount() = mywykopApi.getHashTagsNotificationsCount()
            .retryWhen(userTokenRefresher)
            .compose<NotificationsCountResponse>(ErrorHandlerTransformer())
}