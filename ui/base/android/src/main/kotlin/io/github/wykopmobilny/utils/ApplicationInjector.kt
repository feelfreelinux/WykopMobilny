package io.github.wykopmobilny.utils

import kotlin.reflect.KClass

interface ApplicationInjector {

    fun <T : Any> getDependency(clazz: KClass<T>): T
}
