package io.github.feelfreelinux.wykopmobilny.utils.recyclerview

import android.support.v7.widget.RecyclerView

typealias FABAutoHideListener = (shouldShow : Boolean) -> Unit

class AutoHideFabListener(var hideListener : FABAutoHideListener) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
         hideListener.invoke(dy < 0)
    }
}