package io.github.wykopmobilny.domain.utils

import io.github.wykopmobilny.ui.base.AppDispatchers
import io.github.wykopmobilny.ui.base.ScopedViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class InMemoryScopedViewState @Inject constructor() : ScopedViewState {

    private val mutex = Mutex()

    override val state = MutableStateFlow(value = ScopedViewState.ViewStateUi())

    override suspend fun update(updater: (ScopedViewState.ViewStateUi) -> ScopedViewState.ViewStateUi) = mutex.withLock {
        withContext(AppDispatchers.Default) {
            state.value = updater(state.value)
        }
    }
}
