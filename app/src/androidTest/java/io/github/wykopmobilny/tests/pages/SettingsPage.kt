package io.github.wykopmobilny.tests.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.github.wykopmobilny.tests.matchers.onPreference
import io.github.wykopmobilny.tests.matchers.tapPreference

object SettingsPage {

    private val confirmationOption = withText("Wyłącz potwierdzenie wyjścia z aplikacji")

    fun tapExitConfirmationOption() {
        tapPreference(confirmationOption)
    }

    fun assertConfirmationOptionChecked() {
        onPreference(confirmationOption).check(matches(isChecked()))
    }

    fun assertConfirmationOptionNotChecked() {
        onPreference(confirmationOption).check(matches(isNotChecked()))
    }

    fun tapAppearance() {
        onView(withText("Ustawienia wyglądu aplikacji")).perform(click())
    }

    fun assertVisible() {
        onView(withText("Ustawienia")).check(matches(isDisplayed()))
    }
}
