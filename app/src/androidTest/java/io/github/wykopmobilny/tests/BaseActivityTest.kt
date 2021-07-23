package io.github.wykopmobilny.tests

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.tests.responses.blacklist
import io.github.wykopmobilny.tests.responses.profile
import io.github.wykopmobilny.tests.responses.promoted
import io.github.wykopmobilny.tests.rules.CleanupRule
import io.github.wykopmobilny.tests.rules.MockWebServerRule
import io.github.wykopmobilny.tests.rules.OkHttp3IdlingResource
import io.github.wykopmobilny.ui.login.LoginDependencies
import io.github.wykopmobilny.utils.destroyDependency
import io.github.wykopmobilny.utils.requireDependency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
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
        IdlingRegistry.getInstance().register(CoroutineDispatcherIdlingResource(Dispatchers.Default))
    }

    protected fun loginUser() = runBlocking {
        val dependency = TestApp.instance.requireDependency<LoginDependencies>()
        val login = dependency.login().invoke().first()
        TestApp.instance.cookieProvider.cookies += "http://localhost:8000" to ""
        mockWebServerRule.profile()
        mockWebServerRule.blacklist()
        mockWebServerRule.promoted()
        login.parseUrlAction("https://a2.wykop.pl/ConnectSuccess/appkey/irrelevant/login/fixture-login/token/fixture-token/")
        Espresso.onIdle()
        dependency.login().invoke().filterNot { it.isLoading }.first()
        TestApp.instance.destroyDependency<LoginDependencies>()
        Espresso.onIdle()
    }
}

class CoroutineDispatcherIdlingResource(
    private val dispatcher: CoroutineDispatcher,
) : IdlingResource {
    private var callback: IdlingResource.ResourceCallback? = null

    override fun getName() = "Idling resource for dispatcher=$dispatcher"

    override fun isIdleNow(): Boolean {
        if (dispatcher.isActive) {
            return false
        }

        callback?.onTransitionToIdle()
        return true
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }
}
