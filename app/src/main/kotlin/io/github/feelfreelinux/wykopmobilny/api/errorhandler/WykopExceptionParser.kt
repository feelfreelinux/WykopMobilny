package io.github.feelfreelinux.wykopmobilny.api.errorhandler

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.ApiResponse
import java.io.IOException

class WykopExceptionParser {

    class WykopApiException(val code: Int, override val message: String?) : IOException()

    companion object {
        fun getException(apiResponse: ApiResponse<*>): WykopApiException? {
            apiResponse.error?.apply {
                return WykopApiException(code, messagePl)
            }
            return null
        }
    }
}
