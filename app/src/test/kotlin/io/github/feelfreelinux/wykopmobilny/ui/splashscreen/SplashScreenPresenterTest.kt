package io.github.feelfreelinux.wykopmobilny.ui.splashscreen

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.models.pojo.Profile
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.ui.SubscribeHelperTest
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class SplashScreenPresenterTest {
    lateinit var systemUnderTest: SplashScreenPresenter
    val mockOfView = mock<SplashScreenView>()
    val mockOfUserApi = mock<UserApi>()
    val mockOfApiPreferences = mock<CredentialsPreferencesApi>()

    @Before
    fun setup() {
        systemUnderTest = SplashScreenPresenter(SubscribeHelperTest(), mockOfApiPreferences, mockOfUserApi)
        systemUnderTest.subscribe(mockOfView)

    }

    @Test
    fun shouldOpenLoginScreenWhenNotLoggedIn() {
        whenever(mockOfApiPreferences.isUserAuthorized()).thenReturn(false)
        systemUnderTest.checkIsUserLoggedIn()

        verify(mockOfView).startLoginActivity()
    }

    @Test
    fun shouldSaveTokenAndOpenNavigationScreen() {
        val testProfile = mock<Profile>()
        testProfile.avatarBig = "https://example.com/avatarBig.jpg"
        testProfile.userKey = "samplekey"

        whenever(mockOfUserApi.getUserSessionToken()).thenReturn(Single.just(testProfile))
        whenever(mockOfApiPreferences.isUserAuthorized()).thenReturn(true)

        systemUnderTest.checkIsUserLoggedIn()

        verify(mockOfView).startNavigationActivity()
        verify(mockOfApiPreferences).avatarUrl = testProfile.avatarBig
        verify(mockOfApiPreferences).userToken = testProfile.userKey

    }
}