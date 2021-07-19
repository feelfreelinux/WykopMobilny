package io.github.wykopmobilny.domain.api

import com.dropbox.android.external.store4.FetcherResult
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import io.github.wykopmobilny.api.responses.ApiResponse
import io.github.wykopmobilny.storage.api.LoggedUserInfo
import io.github.wykopmobilny.storage.api.SessionStorage
import io.github.wykopmobilny.storage.api.UserSession
import kotlinx.coroutines.flow.first
import javax.inject.Inject

suspend fun <T : Any, R : Any> apiCall(
    rawCall: suspend () -> ApiResponse<T>,
    mapping: T.() -> R,
): FetcherResult<R> {
    val response = rawCall()
    val error = response.error

    return if (error != null) {
        FetcherResult.Error.Message(error.messagePl)
    } else {
        runCatching { response.data?.mapping() }
            .fold(
                onSuccess = { it?.let { FetcherResult.Data(it) } ?: FetcherResult.Error.Message("Invalid response. App's fault") },
                onFailure = FetcherResult.Error::Exception,
            )
    }
}

internal class ApiClient @Inject constructor(
    private val userSessionStorage: SessionStorage,
    private val userInfoStore: Store<UserSession, LoggedUserInfo>,
) {

    suspend fun <T> fetch(rawCall: suspend () -> ApiResponse<T>): T =
        runCatching { rawCall() }.fold(
            onSuccess = { response ->
                val data = response.data
                if (data == null) {
                    val error = response.error
                    if (error == null) {
                        error("Got empty response")
                    } else {
                        when (error.code) {
                            WykopApiCodes.InvalidUserKey, WykopApiCodes.WrongUserSessionKey -> {
                                val session = userSessionStorage.session.first() ?: error("Login required")
                                userInfoStore.fresh(session)
                                val newResponse = rawCall()
                                newResponse.data
                                    ?: newResponse.error?.let { error("[${it.code}] ${it.messagePl}") }
                                    ?: throw error("Got empty response")
                            }
                            else -> error("[${error.code}] ${error.messagePl}")
                        }
                    }
                } else {
                    data
                }
            },
            onFailure = { throwable -> throw throwable },
        )
}

object WykopApiCodes {
    const val InvalidUserKey = 11
    const val WrongUserSessionKey = 12
}
