package io.github.wykopmobilny.tests

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.wykopmobilny.tests.pages.AppearanceSettingsPage
import io.github.wykopmobilny.tests.pages.DrawerRegion
import io.github.wykopmobilny.tests.pages.MainPage
import io.github.wykopmobilny.tests.pages.SettingsPage
import io.github.wykopmobilny.tests.responses.githubPatronsEmpty
import io.github.wykopmobilny.tests.responses.promoted
import io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest : BaseActivityTest() {

    @Before
    fun setUp() {
        mockWebServerRule.promoted()
        mockWebServerRule.githubPatronsEmpty()
        launchActivity<MainNavigationActivity>()
        MainPage.openDrawer()
        DrawerRegion.tapOption("Ustawienia")
        SettingsPage.assertVisible()
    }

    @Test
    fun general() {
        SettingsPage.assertConfirmationOptionChecked()

        SettingsPage.tapExitConfirmationOption()
        SettingsPage.assertConfirmationOptionNotChecked()

        SettingsPage.tapAppearance()
        Espresso.pressBack()
        SettingsPage.assertConfirmationOptionNotChecked()

        SettingsPage.tapExitConfirmationOption()
        SettingsPage.assertConfirmationOptionChecked()

        SettingsPage.tapAppearance()
        Espresso.pressBack()
        SettingsPage.assertConfirmationOptionChecked()
    }

    @Test
    fun appearance() {
        SettingsPage.tapAppearance()
        AppearanceSettingsPage.assertVisible()
    }
}
