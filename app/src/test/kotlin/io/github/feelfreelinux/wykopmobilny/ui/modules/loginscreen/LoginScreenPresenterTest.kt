package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import com.nhaarman.mockito_kotlin.*
import io.github.feelfreelinux.wykopmobilny.TestSubscriptionHelper
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.LoginCredentials
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class LoginScreenPresenterTest {
    lateinit var systemUnderTest: LoginScreenPresenter
    private val mockOfView = mock<LoginScreenView>()
    private val mockOfUserManager = mock<UserManagerApi>()
    private val mockOfUserApi = mock<UserApi>()
    private val subscriptionHelper = TestSubscriptionHelper()
    private val mockOfApiPreferences = mock<CredentialsPreferencesApi>()

    @Before
    fun setup() {
        systemUnderTest = LoginScreenPresenter(mockOfUserManager, subscriptionHelper, mockOfUserApi)
        systemUnderTest.subscribe(mockOfView)
        whenever(mockOfUserApi.getUserSessionToken()).thenReturn(Single.just(mock()))
    }

    @Test
    fun shouldSaveCredentials() {
        val expectedCredentials = LoginCredentials("feuer", "example_token")

        val url = "https://a.wykop.pl/user/ConnectSuccess/appkey/example_key/login/${expectedCredentials.login}/token/${expectedCredentials.token}/"
        systemUnderTest.handleUrl(url)
        verify(mockOfUserManager).loginUser(expectedCredentials)
    }

    @Test
    fun shouldShowErrorOnEmptyUrl() {
        systemUnderTest.handleUrl("")
        verify(mockOfView).showErrorDialog(any())
        verifyNoMoreInteractions(mockOfApiPreferences)
    }

    @Test
    fun shouldExitActivityOnHandle() {
        val url = "https://a.wykop.pl/user/ConnectSuccess/appkey/example_key/login/example_login/token/example_token/"
        systemUnderTest.handleUrl(url)
        verify(mockOfView, times(1)).goBackToSplashScreen()
    }



}