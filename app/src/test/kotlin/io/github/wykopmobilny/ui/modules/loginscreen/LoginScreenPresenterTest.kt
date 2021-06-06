package io.github.wykopmobilny.ui.modules.loginscreen

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.github.wykopmobilny.api.responses.LoginResponse
import io.github.wykopmobilny.api.responses.ProfileResponse
import io.github.wykopmobilny.api.scraper.ScraperApi
import io.github.wykopmobilny.api.user.LoginApi
import io.github.wykopmobilny.util.rx.TestSchedulerProvider
import io.github.wykopmobilny.utils.usermanager.LoginCredentials
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginScreenPresenterTest {
    lateinit var systemUnderTest: LoginScreenPresenter
    private val mockOfView = mock<LoginScreenView>()
    private val mockOfUserManager = mock<UserManagerApi>()
    private val mockOfUserApi = mock<LoginApi>()
    private val mockOfScraperApi = mock<ScraperApi>()

    lateinit var testSchedulerProvider: TestSchedulerProvider

    @Before
    fun setup() {
        testSchedulerProvider = TestSchedulerProvider()
        systemUnderTest = LoginScreenPresenter(testSchedulerProvider, mockOfUserManager, mockOfScraperApi, mockOfUserApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun shouldSaveCredentials() {
        val profileMock: ProfileResponse = mock()
        val loginResponse = LoginResponse(profileMock, "1")
        val single: Single<LoginResponse> = Single.create {
            emitter ->
            emitter.onSuccess(loginResponse)
        }
        val expectedCredentials = LoginCredentials("feuer", "example_token")

        whenever(mockOfUserApi.getUserSessionToken()).thenReturn(single)

        val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/" +
            "example_key/login/${expectedCredentials.login}/token/${expectedCredentials.token}/"
        systemUnderTest.handleUrl(url)
        testSchedulerProvider.mTestScheduler.triggerActions()

        verify(mockOfUserManager).loginUser(expectedCredentials)
        verify(mockOfView).goBackToSplashScreen()
    }

    @Test
    fun shouldExitActivityOnHandle() {
        val profileMock: ProfileResponse = mock()
        val loginResponse = LoginResponse(profileMock, "1")
        val single: Single<LoginResponse> = Single.create {
            emitter ->
            emitter.onSuccess(loginResponse)
        }

        whenever(mockOfUserApi.getUserSessionToken()).thenReturn(single)

        val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/example_key/login/example_login/token/example_token/"
        systemUnderTest.handleUrl(url)
        systemUnderTest.handleUrl(url)
        testSchedulerProvider.mTestScheduler.triggerActions()

        verify(mockOfView, times(2)).goBackToSplashScreen()
    }
}
