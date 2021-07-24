package io.github.wykopmobilny.tests.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText

object BlacklistPage {

    fun assertVisible() {
        onView(withText("Zarządzaj czarną listą")).check(matches(isDisplayed()))
    }
}
