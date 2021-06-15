package io.github.wykopmobilny.domain.login

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import io.github.wykopmobilny.domain.navigation.AppRestarter
import io.github.wykopmobilny.storage.api.Blacklist
import io.github.wykopmobilny.storage.api.LoggedUserInfo
import io.github.wykopmobilny.storage.api.SessionStorage
import io.github.wykopmobilny.storage.api.UserSession
import io.github.wykopmobilny.ui.base.AppScopes.applicationScope
import io.github.wykopmobilny.ui.base.ScopedViewState
import io.github.wykopmobilny.ui.login.InfoMessageUi
import io.github.wykopmobilny.ui.login.Login
import io.github.wykopmobilny.ui.login.LoginUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginQuery @Inject constructor(
    private val sessionStorage: SessionStorage,
    private val userInfoStore: Store<UserSession, LoggedUserInfo>,
    private val blacklistStore: Store<Unit, Blacklist>,
    private val loginConfig: ConnectConfig,
    private val appRestarter: AppRestarter,
    private val viewState: ScopedViewState,
) : Login {

    override fun invoke(): Flow<LoginUi> = viewState.state.map { viewState ->
        LoginUi(
            urlToLoad = connectUrl(),
            isLoading = viewState.isLoading,
            visibleError = viewState.visibleError?.let {
                InfoMessageUi(
                    title = "Oops...",
                    message = it.message ?: it.toString(),
                    confirmAction = ::dismissError,
                    dismissAction = ::dismissError,
                )
            },
            parseUrlAction = ::onUrlInvoked,
        )
    }

    private fun onUrlInvoked(url: String) = applicationScope.launch {
        val userSession = withContext(Dispatchers.Default) {
            val match = loginPattern.find(url) ?: return@withContext null

            val login = match.groups[1]?.value?.takeIf { it.isNotBlank() }
            val token = match.groups[2]?.value?.takeIf { it.isNotBlank() }

            if (login.isNullOrBlank() || token.isNullOrBlank()) {
                null
            } else {
                UserSession(login, token)
            }
        } ?: return@launch
        viewState.update { it.copy(isLoading = true) }

        runCatching {
            sessionStorage.updateSession(userSession)
            userInfoStore.fresh(userSession)
            blacklistStore.fresh(Unit)
            appRestarter.restart()
        }
            .onFailure { throwable ->
                sessionStorage.updateSession(null)
                userInfoStore.clearAll()
                blacklistStore.clearAll()
                viewState.update { it.copy(isLoading = false, visibleError = throwable) }
            }
            .onSuccess { viewState.update { it.copy(isLoading = false, visibleError = null) } }
    }

    private fun dismissError() = applicationScope.launch {
        viewState.update { it.copy(visibleError = null) }
    }

    private fun connectUrl() = "https://a2.wykop.pl/login/connect/appkey/${loginConfig.appKey}"

    companion object {
        private val loginPattern = "/ConnectSuccess/appkey/.+/login/(.+)/token/(.+)/".toRegex()
    }
}
