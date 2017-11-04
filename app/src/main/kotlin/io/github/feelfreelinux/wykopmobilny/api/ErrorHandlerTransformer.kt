package io.github.feelfreelinux.wykopmobilny.api

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.WykopApiResponse
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import java.io.IOException

class ErrorHandlerTransformer<T : Any> : SingleTransformer<WykopApiResponse<T>, T> {
    class WykopApiException(val code : Int, override val message: String?) : IOException()

    companion object {
        fun<R : WykopApiResponse<R>> applyCompose() : ErrorHandlerTransformer<R> {
            return ErrorHandlerTransformer()
        }
    }

    override fun apply(upstream: Single<WykopApiResponse<T>>): SingleSource<T> {
        return upstream.flatMap {
            if (it.data == null) {
                Single.error(WykopApiException(it.error!!.code, it.error.messagePl))
            } else {
                Single.just(it.data)
            }
        }
    }
}

