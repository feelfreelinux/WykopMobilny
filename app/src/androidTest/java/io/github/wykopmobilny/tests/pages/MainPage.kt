package io.github.wykopmobilny.tests.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import io.github.wykopmobilny.R
import io.github.wykopmobilny.tests.base.Page

object MainPage : Page {

    private val openDrawer = withContentDescription(R.string.nav_drawer_open)
    private val closeDrawer = withContentDescription(R.string.nav_drawer_closed)

    fun assertVisible() {
        onView(openDrawer).check(matches(isDisplayed()))
    }

    fun openDrawer() {
        onView(openDrawer).perform(click())
    }

    fun closeDrawer() {
        onView(closeDrawer).perform(click())
    }
}
