package io.github.feelfreelinux.wykopmobilny.ui.adapters.decorators

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class EntryCommentItemDecoration(val margin : Int) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if(parent?.getChildAdapterPosition(view)!! > 0)
            outRect?.left = margin
    }
}