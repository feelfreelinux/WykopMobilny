package io.github.feelfreelinux.wykopmobilny.ui.helpers

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


class EndlessScrollListener(val linearLayoutManager: LinearLayoutManager, val listener : () -> Unit) : RecyclerView.OnScrollListener() {
    private val visibleThreshold = 2
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        totalItemCount = linearLayoutManager.itemCount
        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
        if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
            // End has been reached
            // Do something
            listener()
        }
    }
}