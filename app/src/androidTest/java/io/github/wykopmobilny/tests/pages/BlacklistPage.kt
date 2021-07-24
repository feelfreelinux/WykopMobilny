package io.github.wykopmobilny.tests.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.github.wykopmobilny.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

object BlacklistPage {

    private val tagsTab = withText("Tagi")
    private val usersTab = withText("Użytkownicy")
    private fun lockIcon(label: String) =
        allOf(
            withId(R.id.btnAction),
            withParent(hasSibling(withText(label))),
        )

    fun tapUsersTab() {
        onView(usersTab).perform(click())
    }

    fun tapTagsTab() {
        onView(tagsTab).perform(click())
    }

    fun tapUnblockTag(tag: String) {
        onView(lockIcon(tag)).perform(click())
    }

    fun tapUnblockUser(user: String) {
        onView(lockIcon(user)).perform(click())
    }

    fun assertVisible() {
        onView(withText("Zarządzaj czarną listą")).check(matches(isDisplayed()))
    }

    fun assertBlockedUserVisible(user: String) {
        onView(usersTab).check(matches(isSelected()))
        onView(withText(user)).check(matches(isDisplayed()))
    }

    fun assertBlockedUserNotVisible(user: String) {
        onView(usersTab).check(matches(isSelected()))
        onView(withText(user)).check(doesNotExist())
    }

    fun assertBlockedTagVisible(tag: String) {
        onView(tagsTab).check(matches(isSelected()))
        onView(withText(tag)).check(matches(isDisplayed()))
    }

    fun assertBlockedTagNotVisible(tag: String) {
        onView(tagsTab).check(matches(isSelected()))
        onView(withText(tag)).check(doesNotExist())
    }
}
