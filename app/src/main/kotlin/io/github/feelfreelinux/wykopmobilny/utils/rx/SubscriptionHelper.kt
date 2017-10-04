package io.github.feelfreelinux.wykopmobilny.utils.rx

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable

interface SubscriptionHelperApi {
    fun <T> subscribe(single : Single<T>, success : (T) -> Unit, exception: (Throwable) -> Unit, subscriber: Any)
    fun dispose(subscriber: Any)
}

class SubscriptionHelper(private val observeScheduler: Scheduler,
                         private val subscribeScheduler: Scheduler) : SubscriptionHelperApi {
    private val subscriptions = HashMap<String, MutableList<Disposable>>()

    override fun <T> subscribe(single : Single<T>, success : (T) -> Unit, exception: (Throwable) -> Unit, subscriber: Any) {
        val disposable = getSubscriberCompositeDisposable(subscriber)
        disposable.add(
                single.observeOn(observeScheduler)
                        .subscribeOn(subscribeScheduler)
                        .subscribe(success, exception)
        )
    }

    private fun getSubscriberCompositeDisposable(subscriber: Any): MutableList<Disposable> {
        var objectSubscriptions = subscriptions[subscriber.uniqueTag]

        if (objectSubscriptions == null) {
            objectSubscriptions = mutableListOf()
            subscriptions.put(subscriber.uniqueTag, objectSubscriptions)
        }
        return objectSubscriptions
    }

    override fun dispose(subscriber: Any) {
        val disposable = subscriptions[subscriber.uniqueTag]

        disposable?.let {
            for (subscription in disposable) {
                subscription.dispose()
            }
        }

        subscriptions.remove(uniqueTag)
    }

    private val Any.uniqueTag : String
        get() = toString()

}