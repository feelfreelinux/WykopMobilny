package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.View

abstract class RecyclableViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    companion object {
        const val SEPARATOR_SMALL = "SEP_SMALL"
        const val SEPARATOR_NORMAL = "SEP_NORMAL"
    }

    open var separatorType = SEPARATOR_SMALL

    abstract fun cleanRecycled()
}