package io.github.wykopmobilny.domain.api

import com.dropbox.android.external.store4.FetcherResult
import io.github.wykopmobilny.api.responses.WykopApiResponse

suspend fun <T : Any, R : Any> apiCall(
    rawCall: suspend () -> WykopApiResponse<T>,
    mapping: T.() -> R,
): FetcherResult<R> {
    val response = rawCall()
    val error = response.error

    return if (error != null) {
        FetcherResult.Error.Message(error.messagePl)
    } else {
        runCatching { response.data?.mapping() }
            .fold(
                onSuccess = { it?.let { FetcherResult.Data(it) } ?: FetcherResult.Error.Message("Invalid response. App's fult") },
                onFailure = FetcherResult.Error::Exception,
            )
    }
}
