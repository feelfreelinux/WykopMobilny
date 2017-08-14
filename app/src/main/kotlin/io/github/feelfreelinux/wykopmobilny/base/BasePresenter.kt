package io.github.feelfreelinux.wykopmobilny.base

interface BasePresenter<in T> {
    fun attachView(view: T)
    fun detachView()
}