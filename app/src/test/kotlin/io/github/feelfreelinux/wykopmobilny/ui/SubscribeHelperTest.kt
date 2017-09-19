package io.github.feelfreelinux.wykopmobilny.ui

import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SubscribeHelperTest : SubscriptionHelperApi {
    override fun <T : Any> subscribeOnSchedulers(single: Single<T>)
        = single.subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())

}