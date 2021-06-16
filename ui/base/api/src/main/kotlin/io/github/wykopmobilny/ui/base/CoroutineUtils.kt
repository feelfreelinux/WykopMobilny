package io.github.wykopmobilny.ui.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

object AppDispatchers {

    lateinit var Default: CoroutineDispatcher
    lateinit var Main: CoroutineDispatcher
    lateinit var IO: CoroutineDispatcher
}

object AppScopes {

    lateinit var applicationScope: CoroutineScope
}
