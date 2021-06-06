package io.github.wykopmobilny.api.errorhandler

import io.github.wykopmobilny.api.responses.ApiResponse
import java.io.IOException

class WykopExceptionParser {

    class WykopApiException(val code: Int, override val message: String?) : IOException()

    companion object {

        fun getException(apiResponse: ApiResponse<*>) = apiResponse.error?.run { WykopApiException(code, messagePl) }
    }
}
