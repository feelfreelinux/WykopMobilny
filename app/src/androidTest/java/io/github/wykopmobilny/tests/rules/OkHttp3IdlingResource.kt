package io.github.wykopmobilny.tests.rules

import androidx.test.espresso.IdlingResource
import okhttp3.Dispatcher
import java.util.concurrent.CopyOnWriteArrayList

class OkHttp3IdlingResource(private val dispatcher: Dispatcher) : IdlingResource {

    private val callbacks = CopyOnWriteArrayList<IdlingResource.ResourceCallback>()

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callbacks.add(callback)
    }

    override fun getName() = "okhttp-idling-resource"

    override fun isIdleNow(): Boolean {
        val isIdle = dispatcher.runningCallsCount() == 0
        if (isIdle) {
            transitionToIdle()
        }

        return isIdle
    }

    private fun transitionToIdle() = callbacks.forEach { it.onTransitionToIdle() }

    init {
        dispatcher.idleCallback = Runnable { transitionToIdle() }
    }
}
