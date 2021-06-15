package io.github.wykopmobilny.ui.base

import kotlinx.coroutines.flow.Flow

interface ScopedViewState {

    val state: Flow<ViewStateUi>

    suspend fun update(updater: (old: ViewStateUi) -> ViewStateUi)

    data class ViewStateUi(
        val isLoading: Boolean = false,
        val visibleError: Throwable? = null,
    )
}
