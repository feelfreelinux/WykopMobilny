package io.github.wykopmobilny.tests

import androidx.test.espresso.IdlingRegistry
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.tests.rules.MockWebServerRule
import io.github.wykopmobilny.tests.rules.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseActivityTest {

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Before
    fun setupIdlingResources() {
        TestApp.instance.androidInjector().inject(this)
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource(okHttpClient.dispatcher))
    }
}
