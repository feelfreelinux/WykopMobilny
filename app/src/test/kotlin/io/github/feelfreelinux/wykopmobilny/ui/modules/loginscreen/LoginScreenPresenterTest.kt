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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import android.R.attr.password
import io.github.feelfreelinux.wykopmobilny.TestSchedulers
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ProfileResponse
import io.github.feelfreelinux.wykopmobilny.util.rx.RxImmediateSchedulerRule
import io.github.feelfreelinux.wykopmobilny.util.rx.TestSchedulerProvider
import io.reactivex.Observable
import org.mockito.MockitoAnnotations
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.trampoline
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginScreenPresenterTest {
	lateinit var systemUnderTest: LoginScreenPresenter
	private val mockOfView = mock<LoginScreenView>()
	private val mockOfUserManager = mock<UserManagerApi>()
//	private val mockOfUserApi = mock<LoginApi>()

	@Mock
	private val mockOfUserApi: LoginApi = mock()
	private val mockOfApiPreferences = mock<CredentialsPreferencesApi>()

//	private var mTestScheduler: TestScheduler? = null

	@get:Rule
	val mOverrideSchedulersRule = RxImmediateSchedulerRule()
	lateinit var testSchedulerProvider: TestSchedulerProvider
	companion object {
		@BeforeClass
		@JvmStatic
		fun intialize() {
			MockitoAnnotations.initMocks(this)
//			RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
//			val mOverrideSchedulersRule = RxImmediateSchedulerRule()
		}

	}


	@Before
	fun setup() {
//		mTestScheduler = TestScheduler()
		testSchedulerProvider = TestSchedulerProvider()
		systemUnderTest = LoginScreenPresenter(testSchedulerProvider, mockOfUserManager, mockOfUserApi)
		systemUnderTest.subscribe(mockOfView)
//		whenever(mockOfUserApi.getUserSessionToken()).thenReturn(Single.just(mock()))
	}

	@Test
	fun shouldSaveCredentials() {
		var loginResponse = LoginResponse(ProfileResponse(),"1")
		val single: Single<LoginResponse> = Single.create {
			emitter ->
			emitter.onSuccess(loginResponse)
		}
		val expectedCredentials = LoginCredentials("feuer", "example_token")

		whenever(mockOfUserApi.getUserSessionToken()).thenReturn(single)

		val url = "https://a2.wykop.pl/user/ConnectSuccess/appkey/example_key/login/${expectedCredentials.login}/token/${expectedCredentials.token}/"
		systemUnderTest.handleUrl(url)
		testSchedulerProvider.mTestScheduler.triggerActions()

	   //mTestScheduler?.triggerActions()

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