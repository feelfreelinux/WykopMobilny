package io.github.wykopmobilny.ui.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SimpleViewStateStorage {

    private val _state = MutableStateFlow(SimpleViewState())
    val state: Flow<SimpleViewState> = _state

    fun update(updater: (old: SimpleViewState) -> SimpleViewState) {
        _state.update(updater)
    }

    data class SimpleViewState(
        val isLoading: Boolean = false,
        val visibleError: Throwable? = null,
    )
}
