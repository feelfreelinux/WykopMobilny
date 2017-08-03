package io.github.feelfreelinux.wykopmobilny.decorators

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class EntryCommentItemDecoration(val margin : Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        if(parent?.getChildAdapterPosition(view)!! > 0)
            outRect?.left = margin
    }
}