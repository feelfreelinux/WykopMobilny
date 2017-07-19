package io.github.feelfreelinux.wykopmobilny.utils

abstract class LoadMoreListener {
    var page = 1
    var loading = false
    abstract fun loadMore ()
}