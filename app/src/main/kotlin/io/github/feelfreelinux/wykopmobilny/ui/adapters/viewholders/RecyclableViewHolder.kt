package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclableViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        const val SEPARATOR_SMALL = "SEP_SMALL"
        const val SEPARATOR_NORMAL = "SEP_NORMAL"
    }
}
