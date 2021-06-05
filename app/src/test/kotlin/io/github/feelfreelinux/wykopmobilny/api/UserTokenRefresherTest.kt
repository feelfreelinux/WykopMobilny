package io.github.feelfreelinux.wykopmobilny.api

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.api.errorhandler.WykopExceptionParser
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationsCountResponse
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import java.io.IOException

class UserTokenRefresherTest {
    lateinit var systemUnderTest: UserTokenRefresher
    val mockOfUserApi = mock<LoginApi>()
    val mockOfMyWykopApi = mock<NotificationsApi>()
    val mockOfUserManager = mock<UserManagerApi>()

    @Before
    fun setup() {
        systemUnderTest = UserTokenRefresher(mockOfUserApi, mockOfUserManager)
    }

    @Test
    fun shouldNotInterceptSuccess() {
        val response = NotificationsCountResponse(15)
        whenever(mockOfMyWykopApi.getNotificationCount()).thenReturn(Single.just(response))
        val testObserver = TestObserver<NotificationsCountResponse>()
        mockOfMyWykopApi
            .getNotificationCount()
            .retryWhen(systemUnderTest)
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.assertComplete()
        testObserver.assertResult(response)
    }

    @Test
    fun shouldNotInterceptEveryException() {
        val exception = IOException()
        whenever(mockOfMyWykopApi.getNotificationCount()).thenReturn(Single.error(exception))
        val testObserver = TestObserver<NotificationsCountResponse>()
        mockOfMyWykopApi
            .getNotificationCount()
            .retryWhen(systemUnderTest)
            .subscribe(testObserver)

        testObserver.assertError(exception)
        verifyZeroInteractions(mockOfUserApi)
        verifyZeroInteractions(mockOfUserManager)
    }

    @Test
    fun shouldNotInterceptEveryApiException() {
        val exception = WykopExceptionParser.WykopApiException(16, "Test")
        whenever(mockOfMyWykopApi.getNotificationCount()).thenReturn(Single.error(exception))
        val testObserver = TestObserver<NotificationsCountResponse>()
        mockOfMyWykopApi
            .getNotificationCount()
            .retryWhen(systemUnderTest)
            .subscribe(testObserver)

        testObserver.assertError(exception)
        verifyZeroInteractions(mockOfUserApi)
        verifyZeroInteractions(mockOfUserManager)
    }
}
