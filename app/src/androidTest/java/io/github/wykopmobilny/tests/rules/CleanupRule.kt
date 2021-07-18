package io.github.wykopmobilny.tests.rules

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import okhttp3.internal.cache2.Relay.Companion.edit
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

internal class CleanupRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                val context = ApplicationProvider.getApplicationContext<Context>()
                listOf(
                    PreferenceManager.getDefaultSharedPreferences(context),
                    context.getSharedPreferences("Preferences", Context.MODE_PRIVATE),
                ).forEach { prefs -> prefs.edit() { clear() } }

                base.evaluate()
            }
        }
}
