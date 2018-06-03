package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class RecyclableViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    companion object {
        val SEPARATOR_SMALL = "SEP_SMALL"
        val SEPARATOR_NORMAL = "SEP_NORMAL"
    }

    open var separatorType = SEPARATOR_SMALL

    abstract fun cleanRecycled()
}