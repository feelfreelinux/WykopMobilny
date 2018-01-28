package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.TestSchedulers
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationsCountResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJobPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice.WykopNotificationsJobView
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.io.IOException

class WykopNotificationsJobPresenterTest {
    lateinit var subjectUnderTest : WykopNotificationsJobPresenter
    val mockUserManager = mock<UserManagerApi>()
    val mockMyWykopApi = mock<NotificationsApi>()
    val schedulers = TestSchedulers()
    val mockView = mock<WykopNotificationsJobView>()

    @Before
    fun setup() {
        subjectUnderTest = WykopNotificationsJobPresenter(schedulers, mockMyWykopApi, mockUserManager)
        subjectUnderTest.subscribe(mockView)
    }

    private fun mockNewNotification(): Notification {
        val mock = mock<Notification>()
        whenever(mock.new).thenReturn(true)
        return mock
    }

    @Test
    fun shouldIgnoreNotAuthorized() {
        whenever(mockUserManager.isUserAuthorized()).thenReturn(false)
        subjectUnderTest.checkNotifications()
        verifyZeroInteractions(mockMyWykopApi, mockView)
    }

    @Test
    fun shouldShowSingleNotification() {
        whenever(mockUserManager.isUserAuthorized()).thenReturn(true)
        val notificationResponse = listOf(mockNewNotification(), mock())
        whenever(mockMyWykopApi.getNotifications(1)).thenReturn(Single.just(notificationResponse))

        subjectUnderTest.checkNotifications()
        verify(mockView).showNotification(notificationResponse.first())
    }

    @Test
    fun shouldShowMultipleNotifications() {
        whenever(mockUserManager.isUserAuthorized()).thenReturn(true)
        val notificationResponse = listOf(mockNewNotification(), mockNewNotification(), mock())
        whenever(mockMyWykopApi.getNotifications(1)).thenReturn(Single.just(notificationResponse))

        subjectUnderTest.checkNotifications()
        verify(mockView).showNotificationsCount(notificationResponse.size - 1)
    }

    @Test
    fun shouldRequestNotificationCountOnFullList() {
        whenever(mockUserManager.isUserAuthorized()).thenReturn(true)
        val notificationResponse = listOf(mockNewNotification(), mockNewNotification())
        val fullNotificationsCount = 9
        whenever(mockMyWykopApi.getNotificationCount()).thenReturn(Single.just(NotificationsCountResponse(fullNotificationsCount)))
        whenever(mockMyWykopApi.getNotifications(1)).thenReturn(Single.just(notificationResponse))

        subjectUnderTest.checkNotifications()
        verify(mockView).showNotificationsCount(fullNotificationsCount)
    }
}