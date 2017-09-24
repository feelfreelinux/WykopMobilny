package io.github.feelfreelinux.wykopmobilny.utils.rx

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

interface SubscriptionHelperApi {
    fun <T : Any> subscribeOnSchedulers(single: Single<T>): Single<T>
}

class SubscriptionHelper : SubscriptionHelperApi {
    override fun <T : Any> subscribeOnSchedulers(single : Single<T>) =
            single
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}