package io.github.wykopmobilny.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.wykopmobilny.tests.pages.BlacklistPage
import io.github.wykopmobilny.tests.pages.DrawerRegion
import io.github.wykopmobilny.tests.pages.MainPage
import io.github.wykopmobilny.tests.pages.SettingsPage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BlacklistTest : BaseActivityTest() {

    @Before
    fun setUp() {
        startLoggedInApp()
        MainPage.openDrawer()
        DrawerRegion.tapOption("Ustawienia")
        SettingsPage.tapBlacklistSettings()
    }

    @Test
    fun appearance() {
        BlacklistPage.assertVisible()
    }
}
