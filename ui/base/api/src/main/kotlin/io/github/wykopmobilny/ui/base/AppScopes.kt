package io.github.wykopmobilny.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.reflect.KClass

interface AppScopes {

    val applicationScope: CoroutineScope

    fun <T : Any> launchScoped(clazz: KClass<T>, block: suspend CoroutineScope.() -> Unit): Job
}

inline fun <reified T : Any> AppScopes.launchIn(noinline block: suspend CoroutineScope.() -> Unit) = launchScoped(T::class, block)
