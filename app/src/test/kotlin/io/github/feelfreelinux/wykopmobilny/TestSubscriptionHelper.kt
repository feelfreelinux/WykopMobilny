package io.github.feelfreelinux.wykopmobilny

import com.nhaarman.mockito_kotlin.mock
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelper
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class TestSubscriptionHelper : SubscriptionHelper(Schedulers.trampoline(), Schedulers.trampoline(), mock()) {
    override fun <T> subscribe(single: Single<T>, success: (T) -> Unit, exception: (Throwable) -> Unit, subscriber: Any) {
        val disposable = getSubscriberCompositeDisposable(subscriber)
            disposable.add(
            single
                    .subscribeOn(subscribeScheduler)
                    .observeOn(observeScheduler)
                    .subscribe(success, exception)
        )
    }
}