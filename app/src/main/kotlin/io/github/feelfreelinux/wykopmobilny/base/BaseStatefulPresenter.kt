package io.github.feelfreelinux.wykopmobilny.base

interface BaseState

// BaseStatefulPresenter.kt
interface BaseStatefulPresenter<in V: BaseView, S: BaseState>: BasePresenter<V> {
    fun subscribe(view: V, state: S?)
    fun getState(): S
}