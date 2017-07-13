package io.github.feelfreelinux.wykopmobilny

abstract class LoadMoreListener {
    var page = 1
    var loading = false
    abstract fun loadMore ()
}