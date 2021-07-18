package io.github.wykopmobilny.tests.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.github.wykopmobilny.tests.base.Page
import org.hamcrest.Matchers.startsWith

object AboutDialog : Page {

    private val appInfo = withText(startsWith("Wykop Mobilny"))

    fun tapAppInfo() {
        onView(appInfo).perform(click())
    }
}
