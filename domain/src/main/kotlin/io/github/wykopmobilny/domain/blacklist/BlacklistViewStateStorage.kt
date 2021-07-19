package io.github.wykopmobilny.domain.blacklist

import io.github.wykopmobilny.domain.blacklist.di.BlacklistScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@BlacklistScope
internal class BlacklistViewStateStorage @Inject constructor() {

    val state = MutableStateFlow(value = BlacklistViewState())

    fun update(updater: (BlacklistViewState) -> BlacklistViewState) {
        state.update(updater)
    }
}

data class BlacklistViewState(
    val isLoading: Boolean = false,
    val visibleError: (ErrorInfo)? = null,
    val tagsState: Map<String, ItemState> = emptyMap(),
    val usersState: Map<String, ItemState> = emptyMap(),
    val unblockConfirmation: List<String> = emptyList(),
) {

    sealed class ItemState {
        object InProgress : ItemState()
        data class Error(val error: Throwable) : ItemState()
    }

    data class ErrorInfo(
        val cause: Throwable,
        val retryAction: () -> Unit,
    )
}
