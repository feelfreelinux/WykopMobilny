package io.github.wykopmobilny.domain.utils

import io.github.wykopmobilny.ui.base.AppScopes
import io.github.wykopmobilny.ui.base.launchIn
import kotlinx.coroutines.CoroutineScope

internal inline fun <reified T : Any> AppScopes.safe(crossinline block: suspend CoroutineScope.() -> Unit) {
    launchIn<T> {
        runCatching { block() }
            .onFailure { /* shhh 🤫 TODO @mk : 18/07/2021 add logging */ }
    }
}
