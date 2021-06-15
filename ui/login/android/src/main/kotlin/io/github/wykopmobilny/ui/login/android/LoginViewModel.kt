package io.github.wykopmobilny.ui.login.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.wykopmobilny.ui.login.Login
import io.github.wykopmobilny.utils.toEvent
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LoginViewModel @Inject constructor(
    login: Login,
) : ViewModel() {

    private val loginData = login().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null,
    )
    val url = loginData.filterNotNull().map { it.urlToLoad.toEvent() }
    val isLoading = loginData.filterNotNull().map { it.isLoading }.distinctUntilChanged()
    val error = loginData.filterNotNull().map { it.visibleError }.distinctUntilChanged()

    fun onNewUrl(url: String) {
        viewModelScope.launch { loginData.value?.parseUrlAction?.invoke(url) }
    }
}
