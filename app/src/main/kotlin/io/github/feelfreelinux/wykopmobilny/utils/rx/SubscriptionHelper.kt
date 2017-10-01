package io.github.feelfreelinux.wykopmobilny.utils.rx

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

interface SubscriptionHelperApi {
    fun <T> subscribe(single : Single<T>, success : (T) -> Unit, exception: (Throwable) -> Unit, subscriber: Any)
    fun dispose(subscriber: Any)
}

class SubscriptionHelper(private val observeScheduler: Scheduler,
                         private val subscribeScheduler: Scheduler) : SubscriptionHelperApi {
    private val subscriptions = HashMap<String, CompositeDisposable>()

    override fun <T> subscribe(single : Single<T>, success : (T) -> Unit, exception: (Throwable) -> Unit, subscriber: Any) {
        val disposable = getSubscriberCompositeDisposable(subscriber)
        disposable.add(
                single.observeOn(observeScheduler)
                        .subscribeOn(subscribeScheduler)
                        .subscribe(success, exception)
        )
    }

    private fun getSubscriberCompositeDisposable(subscriber: Any): CompositeDisposable {
        var compositeDisposable = subscriptions[subscriber.uniqueTag]

        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
            subscriptions.put(subscriber.uniqueTag, compositeDisposable)
        }
        return compositeDisposable
    }

    override fun dispose(subscriber: Any) {
        val disposable = subscriptions[subscriber.uniqueTag]
        disposable?.apply {
            clear()
            subscriptions.remove(subscriber.uniqueTag)
        }
    }

    private val Any.uniqueTag : String
        get() = toString()

}