package io.github.wykopmobilny.tests.pages

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import io.github.wykopmobilny.R
import io.github.wykopmobilny.tests.base.Page

object MainPage : Page {

    private val openDrawer = ViewMatchers.withContentDescription(R.string.nav_drawer_open)
    private val closeDrawer = ViewMatchers.withContentDescription(R.string.nav_drawer_closed)

    fun openDrawer() {
        Espresso.onView(openDrawer).perform(ViewActions.click())
    }

    fun closeDrawer() {
        Espresso.onView(closeDrawer).perform(ViewActions.click())
    }
}
