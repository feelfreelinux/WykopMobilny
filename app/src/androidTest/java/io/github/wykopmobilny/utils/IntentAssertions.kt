package io.github.wykopmobilny.utils

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasDataString
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matcher

object IntentAssertions {

    fun assertIntentMatches(matcher: Matcher<Intent>) = Intents.intended(matcher)
}

fun interceptingIntents(block: IntentAssertions.() -> Unit) {
    Intents.init()
    Intents.intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, Intent()))
    IntentAssertions.block()
    Intents.assertNoUnverifiedIntents()
    Intents.release()
}

fun shareTextIntent(text: String) = allOf(
    hasAction(Intent.ACTION_CHOOSER),
    hasExtra(equalTo(Intent.EXTRA_INTENT), allOf(hasAction(Intent.ACTION_SEND), hasExtra(Intent.EXTRA_TEXT, text))),
)

fun IntentAssertions.assertLinkHandled(link: String) {
    assertIntentMatches(allOf(hasAction(Intent.ACTION_VIEW), hasDataString(equalTo(link))))
}
