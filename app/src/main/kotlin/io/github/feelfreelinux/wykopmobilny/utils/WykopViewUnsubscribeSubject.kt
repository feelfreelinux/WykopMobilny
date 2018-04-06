package io.github.feelfreelinux.wykopmobilny.utils

import io.reactivex.subjects.PublishSubject

class WykopViewUnsubscribeSubject {
    val unsubscribeSubject = PublishSubject.create<Boolean>()
}