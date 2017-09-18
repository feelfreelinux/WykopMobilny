package io.github.feelfreelinux.wykopmobilny.ui.splashscreen

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.moshi.Moshi
import io.github.feelfreelinux.wykopmobilny.api.Profile
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreenPresenterTest {
    lateinit var systemUnderTest: SplashScreenPresenter
    val moshi = Moshi.Builder().build()
    val mockOfView = mock<SplashScreenView>()
    val mockOfUserApi = mock<UserApi>()
    val mockOfApiPreferences = mock<CredentialsPreferencesApi>()

    @Before
    fun setup() {
        systemUnderTest = SplashScreenPresenter(mockOfApiPreferences, mockOfUserApi)
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
        val jsonAdapter = moshi.adapter(Profile::class.java)
        val testProfile = jsonAdapter.fromJson("{}")
        testProfile.avatarBig = "https://example.com/avatarBig.jpg"
        testProfile.userKey = "samplekey"

        val mockOfCall = mock<Call<Profile>>()
        whenever(mockOfCall.enqueue(any())).thenAnswer {
            (it.arguments.first() as Callback<Profile>).onResponse(null, Response.success(testProfile))
        }
        whenever(mockOfUserApi.getUserSessionToken()).thenReturn(mockOfCall)
        whenever(mockOfApiPreferences.isUserAuthorized()).thenReturn(true)

        systemUnderTest.checkIsUserLoggedIn()

        verify(mockOfView).startNavigationActivity()
        verify(mockOfApiPreferences).avatarUrl = testProfile.avatarBig
        verify(mockOfApiPreferences).userToken = testProfile.userKey

    }
}