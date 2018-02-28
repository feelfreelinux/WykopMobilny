package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LoginResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.github.feelfreelinux.wykopmobilny.util.rx.TestSchedulerProvider
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.LoginCredentials
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginScreenPresenterTest {
	lateinit var systemUnderTest: LoginScreenPresenter
	private val mockOfView = mock<LoginScreenView>()
	private val mockOfUserManager = mock<UserManagerApi>()
	private val mockOfUserApi = mock<LoginApi>()
	private val mockOfApiPreferences = mock<CredentialsPreferencesApi>()

	lateinit var testSchedulerProvider: TestSchedulerProvider


	@Before
	fun setup() {
		testSchedulerProvider = TestSchedulerProvider()
		systemUnderTest = LoginScreenPresenter(testSchedulerProvider, mockOfUserManager, mockOfUserApi)
		systemUnderTest.subscribe(mockOfView)
	}

	@Test
	fun shouldSaveCredentials() {
		var profileMock : ProfileResponse = mock()
		val loginResponse = LoginResponse(profileMock,"1")
		val single: Single<LoginResponse> = Single.create {
			emitter ->
			emitter.onSuccess(loginResponse)
		}
		val expectedCredentials = LoginCredentials("feuer", "example_token")

		whenever(mockOfUserApi.getUserSessionToken()).thenReturn(single)

		val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/example_key/login/${expectedCredentials.login}/token/${expectedCredentials.token}/"
		systemUnderTest.handleUrl(url)
		testSchedulerProvider.mTestScheduler.triggerActions()

		verify(mockOfUserManager).loginUser(expectedCredentials)
		verify(mockOfView).goBackToSplashScreen()
	}

	@Test
	fun shouldExitActivityOnHandle() {
		var profileMock : ProfileResponse = mock()
		val loginResponse = LoginResponse(profileMock,"1")
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

	@Test
	fun shouldShowError() {
		var exception = Exception("Error")
		val single: Single<LoginResponse> = Single.error(exception)
		whenever(mockOfUserApi.getUserSessionToken()).thenReturn(single)

		val expectedCredentials = LoginCredentials("feuer", "example_token")
		val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/example_key/login/${expectedCredentials.login}/token/${expectedCredentials.token}/"
		systemUnderTest.handleUrl(url)

		testSchedulerProvider.mTestScheduler.triggerActions()
		verify(mockOfUserManager).loginUser(expectedCredentials)
	}

}