package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.TestSchedulers
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single
import org.junit.Before

class LoginScreenPresenterTest {
    lateinit var systemUnderTest: LoginScreenPresenter
    private val mockOfView = mock<LoginScreenView>()
    private val mockOfUserManager = mock<UserManagerApi>()
    private val mockOfUserApi = mock<LoginApi>()
    private val schedulers = TestSchedulers()
    private val mockOfApiPreferences = mock<CredentialsPreferencesApi>()

    @Before
    fun setup() {
        systemUnderTest = LoginScreenPresenter(schedulers, mockOfUserManager, mockOfUserApi)
        systemUnderTest.subscribe(mockOfView)
        whenever(mockOfUserApi.getUserSessionToken()).thenReturn(Single.just(mock()))
    }

    /*@Test
    fun shouldSaveCredentials() {
        val expectedCredentials = LoginCredentials("feuer", "example_token")

        val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/example_key/login/${expectedCredentials.login}/token/${expectedCredentials.token}/"
        systemUnderTest.handleUrl(url)
        verify(mockOfUserManager).loginUser(expectedCredentials)
    }

    @Test
    fun shouldExitActivityOnHandle() {
        val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/example_key/login/example_login/token/example_token/"
        systemUnderTest.handleUrl(url)
        verify(mockOfView, times(1)).goBackToSplashScreen()
    }*/



}