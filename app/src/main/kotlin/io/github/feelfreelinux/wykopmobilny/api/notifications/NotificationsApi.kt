package io.github.feelfreelinux.wykopmobilny.api.notifications

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationsCountResponse
import io.reactivex.Single

interface NotificationsApi {
    fun getNotificationCount(): Single<NotificationsCountResponse>
    fun getHashTagNotificationCount(): Single<NotificationsCountResponse>
    fun getHashTagNotifications(page : Int): Single<List<Notification>>
    fun getNotifications(page : Int): Single<List<Notification>>
    fun readNotifications(): Single<List<Notification>>
    fun readHashTagNotifications(): Single<List<Notification>>
}