package io.github.feelfreelinux.wykopmobilny.base

import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<T : BaseView> {
    var view : T? = null
    var subscriptions = CompositeDisposable()

    open fun subscribe(view: T) {
        this.view = view
    }

    open fun unsubscribe() {
        view = null
        subscriptions.clear()
    }
}