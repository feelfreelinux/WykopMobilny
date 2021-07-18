package io.github.wykopmobilny.tests.pages

import androidx.drawerlayout.widget.DrawerLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.github.wykopmobilny.tests.base.Page
import org.hamcrest.Matchers.allOf

object DrawerRegion : Page {

    private val root =
        isDescendantOfA(isAssignableFrom(DrawerLayout::class.java))

    private fun option(label: String) =
        allOf(isDescendantOfA(root), withText(label))

    fun tapOption(label: String) {
        onView(option(label)).perform(click())
    }
}
