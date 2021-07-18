package io.github.wykopmobilny.tests.matchers

import android.view.View
import android.widget.CheckBox
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withChild
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

internal fun tapPreference(option: Matcher<View>) {
    onView(option).perform(click())
}

internal fun onPreference(option: Matcher<View>) =
    onView(
        allOf(
            isAssignableFrom(CheckBox::class.java),
            isDescendantOfA(withChild(withChild(option))),
        ),
    )
