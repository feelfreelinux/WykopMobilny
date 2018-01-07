package io.github.feelfreelinux.wykopmobilny.api.errorhandler

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.common.WykopApiResponse
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer

class ErrorHandlerTransformer<T : Any> : SingleTransformer<WykopApiResponse<T>, T> {

    override fun apply(upstream: Single<WykopApiResponse<T>>): SingleSource<T> {
        return upstream.flatMap {
            val exception = WykopExceptionParser.getException(it)
            if (exception != null) Single.error(exception)
            else Single.just(it.data!!)
        }
    }
}