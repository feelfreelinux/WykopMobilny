package io.github.wykopmobilny.tests

import androidx.test.espresso.Espresso
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.tests.responses.blacklist
import io.github.wykopmobilny.tests.responses.callsOnAppStart
import io.github.wykopmobilny.tests.responses.profile
import io.github.wykopmobilny.tests.responses.promoted
import io.github.wykopmobilny.tests.rules.CleanupRule
import io.github.wykopmobilny.tests.rules.IdlingResourcesRule
import io.github.wykopmobilny.tests.rules.MockWebServerRule
import io.github.wykopmobilny.ui.login.LoginDependencies
import io.github.wykopmobilny.utils.destroyDependency
import io.github.wykopmobilny.utils.requireDependency
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.rules.RuleChain

abstract class BaseActivityTest {

    val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val rules: RuleChain = RuleChain.outerRule(IdlingResourcesRule())
        .around(CleanupRule())
        .around(mockWebServerRule)

    protected fun startLoggedInApp() = runBlocking {
        val dependency = TestApp.instance.requireDependency<LoginDependencies>()
        val login = dependency.login().invoke().first()
        TestApp.instance.cookieProvider.cookies += "http://localhost:8000" to ""
        mockWebServerRule.profile()
        mockWebServerRule.blacklist()
        mockWebServerRule.promoted()
        mockWebServerRule.callsOnAppStart()
        login.parseUrlAction("https://a2.wykop.pl/ConnectSuccess/appkey/irrelevant/login/fixture-login/token/fixture-token/")
        Espresso.onIdle()
        dependency.login().invoke().filterNot { it.isLoading }.first()
        TestApp.instance.destroyDependency<LoginDependencies>()
        Espresso.onIdle()
    }
}
