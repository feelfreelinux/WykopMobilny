package io.github.feelfreelinux.wykopmobilny.base

interface BasePresenter<in V: BaseView> {
    fun subscribe(view: V)
    fun unsubscribe()
}