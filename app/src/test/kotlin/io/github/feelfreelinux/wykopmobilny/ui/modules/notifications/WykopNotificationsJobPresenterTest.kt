package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.TestSubscriptionHelper
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationCountResponse
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
    val mockMyWykopApi = mock<MyWykopApi>()
    val subscriptionHelper = TestSubscriptionHelper()
    val mockView = mock<WykopNotificationsJobView>()

    @Before
    fun setup() {
        subjectUnderTest = WykopNotificationsJobPresenter(subscriptionHelper, mockMyWykopApi, mockUserManager)
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
        whenever(mockMyWykopApi.getNotificationCount()).thenReturn(Single.just(NotificationCountResponse(fullNotificationsCount)))
        whenever(mockMyWykopApi.getNotifications(1)).thenReturn(Single.just(notificationResponse))

        subjectUnderTest.checkNotifications()
        verify(mockView).showNotificationsCount(fullNotificationsCount)
    }

    @Test
    fun shouldShowError() {
        val exception = IOException()
        whenever(mockUserManager.isUserAuthorized()).thenReturn(true)
        whenever(mockMyWykopApi.getNotifications(1)).thenReturn(Single.error(exception))

        subjectUnderTest.checkNotifications()
        verify(mockView).showErrorDialog(exception)
    }
}