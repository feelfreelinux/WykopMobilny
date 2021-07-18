package io.github.wykopmobilny.tests.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText

object AppearanceSettingsPage {

    fun assertVisible() {
        onView(withText("Ustawienia")).check(matches(isDisplayed()))
    }
}
