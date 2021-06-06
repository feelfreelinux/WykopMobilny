package io.github.wykopmobilny.api.notifications

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.NotificationsRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.models.dataclass.Notification
import io.github.wykopmobilny.models.mapper.apiv2.NotificationMapper
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val notificationsApi: NotificationsRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher
) : NotificationsApi {

    override fun readNotifications() = notificationsApi.readNotifications()
        .retryWhen(userTokenRefresher)
        .map { emptyList<Notification>() }

    override fun getNotifications(page: Int) = notificationsApi.getNotifications(page)
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getNotificationCount() = notificationsApi.getNotificationCount()
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun readHashTagNotifications() = notificationsApi.readHashTagsNotifications()
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getHashTagNotifications(page: Int) = notificationsApi.getHashTagsNotifications(page)
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getHashTagNotificationCount() = notificationsApi.getHashTagsNotificationsCount()
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
}
