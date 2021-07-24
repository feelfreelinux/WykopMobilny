package io.github.wykopmobilny.ui.base

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.reflect.KClass

interface AppScopes {

    val applicationScope: CoroutineScope

    fun <T : Any> launchScoped(clazz: KClass<T>, block: suspend CoroutineScope.() -> Unit): Job
}

object AppDispatchers {
    val Main = Dispatchers.Main
    var IO = Dispatchers.IO
        private set
    var Default = Dispatchers.Default
        private set

    @VisibleForTesting
    fun replaceDispatchers(
        io: CoroutineDispatcher = Dispatchers.IO,
        default: CoroutineDispatcher = Dispatchers.Default,
    ) {
        IO = io
        Default = default
    }
}

inline fun <reified T : Any> AppScopes.launchIn(noinline block: suspend CoroutineScope.() -> Unit) = launchScoped(T::class, block)
