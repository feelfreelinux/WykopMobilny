package io.github.feelfreelinux.wykopmobilny.ui.helpers

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class EndlessScrollListener(val linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager, val listener : () -> Unit) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    private val visibleThreshold = 2
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
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