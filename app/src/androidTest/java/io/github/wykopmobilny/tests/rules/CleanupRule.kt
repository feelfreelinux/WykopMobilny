package io.github.wykopmobilny.tests.rules

import android.content.Context
import android.webkit.CookieManager
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class CleanupRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                val context = ApplicationProvider.getApplicationContext<Context>()
                listOf(
                    PreferenceManager.getDefaultSharedPreferences(context),
                    context.getSharedPreferences("Preferences", Context.MODE_PRIVATE),
                ).forEach { prefs -> prefs.edit() { clear() } }
                runBlocking {
                    withContext(Dispatchers.Main) {
                        suspendCoroutine<Unit> { continuation ->
                            CookieManager.getInstance().removeAllCookies { continuation.resume(Unit) }
                        }
                    }
                }

                base.evaluate()
            }
        }
}
