package io.github.wykopmobilny.tests

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.tests.pages.DrawerRegion
import io.github.wykopmobilny.tests.pages.MainPage
import io.github.wykopmobilny.tests.responses.blacklist
import io.github.wykopmobilny.tests.responses.connectPage
import io.github.wykopmobilny.tests.responses.profile
import io.github.wykopmobilny.tests.responses.promoted
import io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest : BaseActivityTest() {

    @Test
    fun navigation() {
        mockWebServerRule.promoted()
        launchActivity<MainNavigationActivity>()
        Espresso.onIdle()

        MainPage.openDrawer()

        mockWebServerRule.connectPage()
        mockWebServerRule.profile()
        TestApp.instance.cookieProvider.cookies += "http://localhost:8000" to ""
        mockWebServerRule.blacklist()
        mockWebServerRule.promoted()

        DrawerRegion.tapOption("Zaloguj się")
        Espresso.onIdle()

        MainPage.assertVisible()
        MainPage.openDrawer()
        DrawerRegion.tapOption("Wyloguj się")
    }
}
