package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class RecyclableViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    abstract fun cleanRecycled()
}