package io.github.feelfreelinux.wykopmobilny.utils.rx

import io.github.feelfreelinux.wykopmobilny.api.UserTokenRefresher
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Function
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

interface SubscriptionHelperApi {
    fun <T> subscribe(single : Single<T>, success : (T) -> Unit, exception: (Throwable) -> Unit, subscriber: Any)
    fun dispose(subscriber: Any)
    fun getSubscriberCompositeDisposable(subscriber: Any): MutableList<Disposable>
}

open class SubscriptionHelper(internal val observeScheduler: Scheduler,
                              internal val subscribeScheduler: Scheduler,
                              val userTokenRefresher: UserTokenRefresher) : SubscriptionHelperApi {
    private val subscriptions = HashMap<String, MutableList<Disposable>>()

    override fun <T> subscribe(single : Single<T>, success : (T) -> Unit, exception: (Throwable) -> Unit, subscriber: Any) {
        val disposable = getSubscriberCompositeDisposable(subscriber)
        disposable.add(
                single
                        .retryWhen(userTokenRefresher)
                        .observeOn(observeScheduler)
                        .subscribeOn(subscribeScheduler)
                        .subscribe(success, exception)
        )
    }

    override fun getSubscriberCompositeDisposable(subscriber: Any): MutableList<Disposable> {
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