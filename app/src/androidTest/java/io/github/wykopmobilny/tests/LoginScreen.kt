package io.github.wykopmobilny.tests

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.wykopmobilny.tests.pages.DrawerRegion
import io.github.wykopmobilny.tests.pages.MainPage
import io.github.wykopmobilny.tests.responses.promotedEmpty
import io.github.wykopmobilny.tests.rules.MockWebServerRule
import io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreen {

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    @Test
    fun navigation() {
        mockWebServerRule.promotedEmpty()
        launchActivity<MainNavigationActivity>()
        Espresso.onIdle()

        MainPage.openDrawer()
        DrawerRegion.tapOption("Zaloguj się")

        Espresso.onIdle()
    }
}
