package io.github.wykopmobilny.tests

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.wykopmobilny.ui.modules.mainnavigation.MainNavigationActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SampleTest {

    @Test
    fun name() {
        launchActivity<MainNavigationActivity>()
        Espresso.onIdle()

        onView(withText("OK")).perform(click())
    }
}
