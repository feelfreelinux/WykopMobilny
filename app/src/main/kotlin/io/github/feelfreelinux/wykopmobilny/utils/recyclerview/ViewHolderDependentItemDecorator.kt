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
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
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
        theme.resolveAttribute(R.attr.lineColor, typedValue, true)
        paint.style = Paint.Style.FILL
        paint.color = typedValue.data

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val position = parent.getChildAdapterPosition(view)
        val viewType = parent.adapter.getItemViewType(position)

        if (viewType == EntryDetailAdapter.ENTRY_HOLDER || viewType == LinkDetailsAdapter.HEADER_HOLDER || viewType == LinkAdapter.LINK_VIEWTYPE || viewType == LinkAdapter.SIMPLE_LINK_VIEWTYPE) {
            outRect.set(0, 0, 0, largeHeight)
        } else if (viewType == LinkDetailsAdapter.COMMENT_HOLDER || viewType == LinkDetailsAdapter.TOP_COMMENT_HOLDER) {
            if (view.tag == LinkDetailsAdapter.COMMENT_TYPE_NORMAL) {
                outRect.set(0, 0, 0, normalHeight)
            } else if (view.tag == LinkDetailsAdapter.COMMENT_TYPE_LARGE) {
                outRect.set(0, 0, 0, largeHeight)
            }
        } else if (viewType != SimpleBaseProgressAdapter.ITEM_PROGRESS) {
            outRect.set(0, 0, 0, normalHeight)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)
            if (position > -1 && parent.adapter.itemCount >= position) {
                val viewType = parent.adapter.getItemViewType(position)
                if (viewType == EntryDetailAdapter.ENTRY_HOLDER || viewType == LinkDetailsAdapter.HEADER_HOLDER || viewType == LinkAdapter.LINK_VIEWTYPE || viewType == LinkAdapter.SIMPLE_LINK_VIEWTYPE) {
                    c.drawRect(view.left.toFloat(), view.bottom.toFloat(), view.right.toFloat(), (view.bottom + largeHeight).toFloat(), paint)
                } else if (viewType == LinkDetailsAdapter.COMMENT_HOLDER || viewType == LinkDetailsAdapter.TOP_COMMENT_HOLDER) {
                    if (view.tag == LinkDetailsAdapter.COMMENT_TYPE_NORMAL) {
                        c.drawRect(view.left.toFloat(), view.bottom.toFloat(), view.right.toFloat(), (view.bottom + normalHeight).toFloat(), paint)
                    } else if (view.tag == LinkDetailsAdapter.COMMENT_TYPE_LARGE) {
                        c.drawRect(view.left.toFloat(), view.bottom.toFloat(), view.right.toFloat(), (view.bottom + largeHeight).toFloat(), paint)
                    }
                } else if (viewType != SimpleBaseProgressAdapter.ITEM_PROGRESS) {
                    c.drawRect(view.left.toFloat(), view.bottom.toFloat(), view.right.toFloat(), (view.bottom + normalHeight).toFloat(), paint)
                }
            }
        }
    }

}