package io.github.feelfreelinux.wykopmobilny.base

open class BasePresenter<T : BaseView> {
    var view : T? = null

    open fun subscribe(view: T) {
        this.view = view
    }

    open fun unsubscribe() {
        view = null
    }
}