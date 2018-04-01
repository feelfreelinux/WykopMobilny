package io.github.feelfreelinux.wykopmobilny.utils.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import android.support.annotation.ColorInt
import android.content.res.Resources.Theme
import io.github.feelfreelinux.wykopmobilny.ui.adapters.EntryDetailAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkAdapter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.LinkDetailsAdapter




class ViewHolderDependentItemDecorator(val context: Context) : RecyclerView.ItemDecoration() {
    val paint by lazy { Paint() }
    val largeHeight by lazy {
        context.resources.getDimension(R.dimen.separator_large).toInt()
    }

    val normalHeight by lazy {
        context.resources.getDimension(R.dimen.separator_normal).toInt()
    }

    init {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.separatorColor, typedValue, true)
        paint.style = Paint.Style.FILL
        paint.color = typedValue.data

    }

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView, state: RecyclerView.State?) {
        val position = parent.getChildAdapterPosition(view)
        val viewType = parent.adapter.getItemViewType(position)
        if (viewType == EntryDetailAdapter.COMMENT_HOLDER || viewType == LinkDetailsAdapter.COMMENT_HOLDER || viewType == LinkDetailsAdapter.TOP_COMMENT_HOLDER) {
            outRect.set(0, 0, 0, normalHeight)
        } else {
            outRect.set(0, 0, 0, largeHeight)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.getChildCount()) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (position > -1 && parent.adapter.itemCount >= position) {
                var viewType = parent.getAdapter().getItemViewType(position)
                if (viewType == EntryDetailAdapter.COMMENT_HOLDER || viewType == LinkDetailsAdapter.COMMENT_HOLDER || viewType == LinkDetailsAdapter.TOP_COMMENT_HOLDER) {
                    c.drawRect(view.left.toFloat(), view.bottom.toFloat(), view.right.toFloat(), (view.bottom + normalHeight).toFloat(), paint)
                } else {
                    c.drawRect(view.left.toFloat(), view.bottom.toFloat(), view.right.toFloat(), (view.bottom + largeHeight).toFloat(), paint)
                }
            }
        }
    }

}