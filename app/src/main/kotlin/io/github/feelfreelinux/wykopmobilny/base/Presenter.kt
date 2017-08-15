package io.github.feelfreelinux.wykopmobilny.base

abstract class Presenter<T : BaseView> : BasePresenter<T> {
    var view : T? = null

    override fun subscribe(view: T) {
        this.view = view
    }

    override fun unsubscribe() {
        view = null
    }
}