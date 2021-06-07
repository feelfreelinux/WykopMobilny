package io.github.wykopmobilny.tests.rules

import androidx.test.espresso.IdlingResource
import okhttp3.Dispatcher
import java.util.UUID

class OkHttp3IdlingResource(private val dispatcher: Dispatcher) : IdlingResource {

    @Volatile
    var callback: IdlingResource.ResourceCallback? = null

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    override fun getName() = UUID.randomUUID().toString()

    override fun isIdleNow() = dispatcher.runningCallsCount() == 0

    init {
        dispatcher.idleCallback = Runnable { callback?.onTransitionToIdle() }
    }
}
