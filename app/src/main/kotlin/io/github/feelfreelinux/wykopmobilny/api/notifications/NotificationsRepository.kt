package io.github.feelfreelinux.wykopmobilny.api.notifications

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2.NotificationMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationsCountResponse
import retrofit2.Retrofit

class NotificationsRepository(
    val retrofit: Retrofit,
    val userTokenRefresher: UserTokenRefresher
) : NotificationsApi {

    private val notificationsApi by lazy { retrofit.create(NotificationsRetrofitApi::class.java) }

    override fun readNotifications() = notificationsApi.readNotifications()
        .retryWhen(userTokenRefresher)
        .map { emptyList<Notification>() }

    override fun getNotifications(page: Int) = notificationsApi.getNotifications(page)
        .retryWhen(userTokenRefresher)
        .compose<List<NotificationResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getNotificationCount() = notificationsApi.getNotificationCount()
        .retryWhen(userTokenRefresher)
        .compose<NotificationsCountResponse>(ErrorHandlerTransformer())

    override fun readHashTagNotifications() = notificationsApi.readHashTagsNotifications()
        .retryWhen(userTokenRefresher)
        .compose<List<NotificationResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getHashTagNotifications(page: Int) = notificationsApi.getHashTagsNotifications(page)
        .retryWhen(userTokenRefresher)
        .compose<List<NotificationResponse>>(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getHashTagNotificationCount() = notificationsApi.getHashTagsNotificationsCount()
        .retryWhen(userTokenRefresher)
        .compose<NotificationsCountResponse>(ErrorHandlerTransformer())
}