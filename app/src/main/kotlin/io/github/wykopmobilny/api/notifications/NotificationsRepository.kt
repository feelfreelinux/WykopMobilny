package io.github.wykopmobilny.api.notifications

import io.github.wykopmobilny.api.UserTokenRefresher
import io.github.wykopmobilny.api.endpoints.NotificationsRetrofitApi
import io.github.wykopmobilny.api.errorhandler.ErrorHandlerTransformer
import io.github.wykopmobilny.models.dataclass.Notification
import io.github.wykopmobilny.models.mapper.apiv2.NotificationMapper
import kotlinx.coroutines.rx2.rxSingle
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val notificationsApi: NotificationsRetrofitApi,
    private val userTokenRefresher: UserTokenRefresher
) : NotificationsApi {

    override fun readNotifications() = rxSingle { notificationsApi.readNotifications() }
        .retryWhen(userTokenRefresher)
        .map { emptyList<Notification>() }

    override fun getNotifications(page: Int) = rxSingle { notificationsApi.getNotifications(page) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getNotificationCount() = rxSingle { notificationsApi.getNotificationCount() }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())

    override fun readHashTagNotifications() = rxSingle { notificationsApi.readHashTagsNotifications() }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getHashTagNotifications(page: Int) = rxSingle { notificationsApi.getHashTagsNotifications(page) }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
        .map { it.map { response -> NotificationMapper.map(response) } }

    override fun getHashTagNotificationCount() = rxSingle { notificationsApi.getHashTagsNotificationsCount() }
        .retryWhen(userTokenRefresher)
        .compose(ErrorHandlerTransformer())
}
