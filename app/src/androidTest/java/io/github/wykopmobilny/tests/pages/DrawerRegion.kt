package io.github.wykopmobilny.tests.pages

import androidx.drawerlayout.widget.DrawerLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import io.github.wykopmobilny.tests.base.Page
import org.hamcrest.Matchers

object DrawerRegion : Page {

    private val root =
        ViewMatchers.isDescendantOfA(ViewMatchers.isAssignableFrom(DrawerLayout::class.java))

    private fun option(label: String) =
        Matchers.allOf(ViewMatchers.isDescendantOfA(root), ViewMatchers.withText(label))

    fun tapOption(label: String) {
        Espresso.onView(option(label)).perform(ViewActions.click())
    }
}
