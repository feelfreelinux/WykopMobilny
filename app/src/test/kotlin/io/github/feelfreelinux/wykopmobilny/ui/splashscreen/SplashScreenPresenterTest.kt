package io.github.feelfreelinux.wykopmobilny.ui.splashscreen

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.models.pojo.Profile
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelper
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class SplashScreenPresenterTest {
    lateinit var systemUnderTest: SplashScreenPresenter
    val mockOfView = mock<SplashScreenView>()
    val mockOfUserApi = mock<UserApi>()
    val mockOfUserManager = mock<UserManagerApi>()
    val subscribeHelper = SubscriptionHelper(Schedulers.trampoline(), Schedulers.trampoline())

    @Before
    fun setup() {
        systemUnderTest = SplashScreenPresenter(subscribeHelper, mockOfUserManager, mockOfUserApi)
        systemUnderTest.subscribe(mockOfView)

    }

    @Test
    fun shouldOpenNavigatuinScreenWhenNotLoggedIn() {
        whenever(mockOfUserManager.isUserAuthorized()).thenReturn(false)
        systemUnderTest.checkIsUserLoggedIn()
        verify(mockOfView).startNavigationActivity()
    }

    @Test
    fun shouldSaveTokenAndOpenNavigationScreen() {
        val testProfile = mock<Profile>()
        testProfile.avatarBig = "https://example.com/avatarBig.jpg"
        testProfile.userKey = "samplekey"

        whenever(mockOfUserApi.getUserSessionToken()).thenReturn(Single.just(testProfile))
        whenever(mockOfUserManager.isUserAuthorized()).thenReturn(true)

        systemUnderTest.checkIsUserLoggedIn()

        verify(mockOfView).startNavigationActivity()
        verify(mockOfUserManager).saveCredentials(testProfile)

    }
}