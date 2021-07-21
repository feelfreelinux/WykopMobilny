package io.github.wykopmobilny.utils

import android.content.Context

inline fun <reified T : Any> Context.requireDependency() =
    (applicationContext as ApplicationInjector).getDependency(T::class)

inline fun <reified T : Any> Context.destroyDependency() =
    (applicationContext as ApplicationInjector).destroyDependency(T::class)
