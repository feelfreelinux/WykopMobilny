package io.github.feelfreelinux.wykopmobilny.utils.recyclerview

import androidx.recyclerview.widget.RecyclerView

typealias FABAutoHideListener = (shouldShow : Boolean) -> Unit

class AutoHideFabListener(var hideListener : FABAutoHideListener) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
         hideListener.invoke(dy < 0)
    }
}