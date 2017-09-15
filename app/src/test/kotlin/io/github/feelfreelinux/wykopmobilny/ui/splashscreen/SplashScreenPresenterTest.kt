package io.github.feelfreelinux.wykopmobilny.ui.splashscreen

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import io.github.feelfreelinux.wykopmobilny.api.ApiResultCallback
import io.github.feelfreelinux.wykopmobilny.api.Profile
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.utils.api.IApiPreferences
import org.junit.Before
import org.junit.Test

class SplashScreenPresenterTest {
    lateinit var systemUnderTest: SplashScreenPresenter
    val mockOfView = mock<SplashScreenView>()
    val mockOfWykopApi = mock<WykopApi>()
    val mockOfApiPreferences = mock<IApiPreferences>()

    @Before
    fun setup() {
        systemUnderTest = SplashScreenPresenter(mockOfApiPreferences, mockOfWykopApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun shouldOpenLoginScreenWhenNotLoggedIn() {
        whenever(mockOfApiPreferences.isUserAuthorized()).thenReturn(false)
        systemUnderTest.checkIsUserLoggedIn()

        verify(mockOfView, times(1)).startLoginActivity()
    }

    @Test
    fun shouldSaveTokenAndOpenNavigationScreen() {
        val testProfile = Gson().fromJson("{}", Profile::class.java)
        testProfile.avatarBig = "https://example.com/avatarBig.jpg"
        testProfile.userKey = "samplekey"

        val result = Result.of(testProfile) as Result<Profile, FuelError>

        whenever(mockOfApiPreferences.isUserAuthorized()).thenReturn(true)
        whenever(mockOfWykopApi.getUserSessionToken(any())).thenAnswer{
            (it.arguments.first() as ApiResultCallback<Profile>).invoke(result)
            Request()
        }
        systemUnderTest.checkIsUserLoggedIn()

        verify(mockOfView, times(1)).startNavigationActivity()
        verify(mockOfApiPreferences, times(1)).avatarUrl = testProfile.avatarBig
        verify(mockOfApiPreferences, times(1)).userToken = testProfile.userKey

    }


}