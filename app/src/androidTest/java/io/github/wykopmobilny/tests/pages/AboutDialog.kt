package io.github.wykopmobilny.tests.pages

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import io.github.wykopmobilny.tests.base.Page
import org.hamcrest.Matchers

object AboutDialog : Page {

    private val appInfo = ViewMatchers.withText(Matchers.startsWith("Wykop Mobilny"))

    fun tapAppInfo() {
        Espresso.onView(appInfo).perform(ViewActions.click())
    }
}
