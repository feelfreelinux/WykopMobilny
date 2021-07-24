package io.github.wykopmobilny.tests.rules

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import io.github.wykopmobilny.TestApp
import io.github.wykopmobilny.ui.base.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.coroutines.CoroutineContext

class IdlingResourcesRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement {

        return object : Statement() {
            override fun evaluate() {
                val idlingRegistry = IdlingRegistry.getInstance()
                val okHttp3IdlingResource = OkHttp3IdlingResource(TestApp.instance.okHttpClient.dispatcher)

                idlingRegistry.register(okHttp3IdlingResource)
                base.evaluate()
                idlingRegistry.unregister(okHttp3IdlingResource)
            }
        }
    }
}

class DispatcherIdlerRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement =
        object : Statement() {
            override fun evaluate() {
                val espressoTrackedDispatcherIO = EspressoTrackedDispatcher(Dispatchers.IO)
                val espressoTrackedDispatcherDefault = EspressoTrackedDispatcher(Dispatchers.Default)
                AppDispatchers.replaceDispatchers(
                    io = espressoTrackedDispatcherIO,
                    default = espressoTrackedDispatcherDefault,
                )
                try {
                    base?.evaluate()
                } finally {
                    espressoTrackedDispatcherIO.cleanUp()
                    espressoTrackedDispatcherDefault.cleanUp()
                    AppDispatchers.replaceDispatchers()
                }
            }
        }
}

class EspressoTrackedDispatcher(private val wrappedCoroutineDispatcher: CoroutineDispatcher) : CoroutineDispatcher() {
    private val counter = CountingIdlingResource("EspressoTrackedDispatcher for $wrappedCoroutineDispatcher")

    init {
        IdlingRegistry.getInstance().register(counter)
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        counter.increment()
        val blockWithDecrement = Runnable {
            try {
                block.run()
            } finally {
                counter.decrement()
            }
        }
        wrappedCoroutineDispatcher.dispatch(context, blockWithDecrement)
    }

    fun cleanUp() {
        IdlingRegistry.getInstance().unregister(counter)
    }
}
