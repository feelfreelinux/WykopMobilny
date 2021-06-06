package io.github.wykopmobilny.api.notifications

import io.github.wykopmobilny.api.responses.NotificationsCountResponse
import io.github.wykopmobilny.models.dataclass.Notification
import io.reactivex.Single

interface NotificationsApi {
    fun getNotificationCount(): Single<NotificationsCountResponse>
    fun getHashTagNotificationCount(): Single<NotificationsCountResponse>
    fun getHashTagNotifications(page: Int): Single<List<Notification>>
    fun getNotifications(page: Int): Single<List<Notification>>
    fun readNotifications(): Single<List<Notification>>
    fun readHashTagNotifications(): Single<List<Notification>>
}
