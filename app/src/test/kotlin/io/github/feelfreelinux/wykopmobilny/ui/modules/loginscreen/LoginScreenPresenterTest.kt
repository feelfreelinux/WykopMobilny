package io.github.feelfreelinux.wykopmobilny.ui.modules.loginscreen

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.api.user.LoginApi
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LoginResponse
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.LoginCredentials
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subscribers.TestSubscriber
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import android.R.attr.password
import io.github.feelfreelinux.wykopmobilny.TestSchedulers
import io.github.feelfreelinux.wykopmobilny.util.rx.RxImmediateSchedulerRule
import io.github.feelfreelinux.wykopmobilny.util.rx.TestSchedulerProvider
import io.reactivex.Observable
import org.junit.ClassRule
import org.mockito.MockitoAnnotations


class LoginScreenPresenterTest {
	lateinit var systemUnderTest: LoginScreenPresenter
	private val mockOfView = mock<LoginScreenView>()
	private val mockOfUserManager = mock<UserManagerApi>()
	private val mockOfUserApi = mock<LoginApi>()
	private val mockOfApiPreferences = mock<CredentialsPreferencesApi>()

	private var mTestScheduler: TestScheduler? = null

	companion object {
		@ClassRule
		@JvmField
		val schedulers = RxImmediateSchedulerRule()
	}

	@Before
	fun setup() {
		MockitoAnnotations.initMocks(this)

		mTestScheduler = TestScheduler()
		var testSchedulerProvider = TestSchedulerProvider(mTestScheduler!!)
		systemUnderTest = LoginScreenPresenter(testSchedulerProvider, mockOfUserManager, mockOfUserApi)
		systemUnderTest.subscribe(mockOfView)
//		whenever(mockOfUserApi.getUserSessionToken()).thenReturn(Single.just(mock()))
	}

	@Test
	fun shouldSaveCredentials() {
		val expectedCredentials = LoginCredentials("feuer", "example_token")

		var result: Single<LoginResponse> = mock()
		doReturn(result)
				.`when`(mockOfUserApi)
				.getUserSessionToken()

		val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/example_key/login/${expectedCredentials.login}/token/${expectedCredentials.token}/"
		systemUnderTest.handleUrl(url)
		mTestScheduler?.triggerActions()

		verify(mockOfUserManager).loginUser(expectedCredentials)
		verify(mockOfView).goBackToSplashScreen()
	}

	@Test
	fun shouldExitActivityOnHandle() {
		val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/example_key/login/example_login/token/example_token/"
		systemUnderTest.handleUrl(url)
		verify(mockOfView, times(1)).goBackToSplashScreen()
	}


}