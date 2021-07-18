package io.github.wykopmobilny.tests

import androidx.test.espresso.IdlingRegistry
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.tests.rules.CleanupRule
import io.github.wykopmobilny.tests.rules.MockWebServerRule
import io.github.wykopmobilny.tests.rules.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Rule
import org.junit.rules.RuleChain
import javax.inject.Inject

abstract class BaseActivityTest {

    val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val rules: RuleChain = RuleChain.outerRule(mockWebServerRule)
        .around(CleanupRule())

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setupIdlingResources() {
        okHttpClient = TestApp.instance.okHttpClient
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource(okHttpClient.dispatcher))
    }
}
