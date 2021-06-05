package io.github.feelfreelinux.wykopmobilny.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.intoComposite(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}
