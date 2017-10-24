package io.github.feelfreelinux.wykopmobilny.api.mywykop

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.mapper.NotificationMapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationCountResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationResponse
import io.reactivex.Single
import retrofit2.Retrofit

interface MyWykopApi {
    fun getNotificationCount(): Single<NotificationCountResponse>
    fun getHashTagNotificationCount(): Single<NotificationCountResponse>
    fun getHashTagNotifications(page : Int): Single<List<Notification>>
    fun getNotifications(page : Int): Single<List<Notification>>
    fun readNotifications(): Single<List<NotificationResponse>>
    fun readHashTagNotifications(): Single<List<NotificationResponse>>
}

class MyWykopRepository(val retrofit: Retrofit) : MyWykopApi {
    private val mywykopApi by lazy { retrofit.create(MyWykopRetrofitApi::class.java) }

    override fun readNotifications() = mywykopApi.readNotifications()

    override fun getNotifications(page : Int) = mywykopApi.getNotifications(page).map { it.map { NotificationMapper.map(it) } }

    override fun getNotificationCount() = mywykopApi.getNotificationCount()

    override fun readHashTagNotifications() = mywykopApi.readHashTagsNotifications()

    override fun getHashTagNotifications(page : Int) = mywykopApi.getHashTagsNotifications(page).map { it.map { NotificationMapper.map(it) } }

    override fun getHashTagNotificationCount() = mywykopApi.getHashTagsNotificationsCount()
}