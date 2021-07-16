package io.github.wykopmobilny.tests

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.tests.pages.DrawerRegion
import io.github.wykopmobilny.tests.pages.MainPage
import io.github.wykopmobilny.tests.responses.login
import io.github.wykopmobilny.tests.responses.promotedEmpty
import io.github.wykopmobilny.ui.login.LoginDependencies
import io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import io.github.wykopmobilny.utils.requireDependency
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest : BaseActivityTest() {

    @Test
    fun navigation() {
        mockWebServerRule.promotedEmpty()
        launchActivity<MainNavigationActivity>()
        Espresso.onIdle()

        MainPage.openDrawer()
        DrawerRegion.tapOption("Zaloguj się")

        Espresso.onIdle()

        mockWebServerRule.login()
        triggerArtificialLogin()
        Espresso.onIdle()
    }

    private fun triggerArtificialLogin() = runBlocking {
        val loginUrl = "https://a2.wykop.pl/zaloguj/ConnectSuccess/appkey/app-key/login/user-login/token/user-token/"
        TestApp.instance.requireDependency<LoginDependencies>().login().invoke().first().parseUrlAction(loginUrl)
    }
}
