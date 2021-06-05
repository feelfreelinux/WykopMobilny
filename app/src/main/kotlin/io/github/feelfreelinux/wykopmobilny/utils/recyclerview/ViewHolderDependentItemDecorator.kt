package io.github.feelfreelinux.wykopmobilny.utils.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder

class ViewHolderDependentItemDecorator(val context: Context) : RecyclerView.ItemDecoration() {

    val paint by lazy { Paint() }
    private val largeHeight by lazy { context.resources.getDimension(R.dimen.separator_large).toInt() }
    private val normalHeight by lazy { context.resources.getDimension(R.dimen.separator_normal).toInt() }

    init {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.lineColor, typedValue, true)
        paint.style = Paint.Style.FILL
        paint.color = typedValue.data
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        try {
            if (view.tag == RecyclableViewHolder.SEPARATOR_NORMAL) {
                outRect.set(0, 0, 0, largeHeight)
            } else {
                outRect.set(0, 0, 0, normalHeight)
            }
        } catch (exception: Throwable) {
            Log.w(this::class.simpleName, "Couldn't get item offset", exception)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        try {
            super.onDraw(c, parent, state)
            for (i in 0 until parent.childCount) {
                val view = parent.getChildAt(i)
                val position = parent.getChildAdapterPosition(view)
                if (position > -1 && parent.adapter != null && parent.adapter!!.itemCount >= position) {
                    if (view.tag == RecyclableViewHolder.SEPARATOR_NORMAL) {
                        c.drawRect(
                            view.left.toFloat(),
                            view.bottom.toFloat(),
                            view.right.toFloat(),
                            (view.bottom + largeHeight).toFloat(),
                            paint,
                        )
                    } else {
                        c.drawRect(
                            view.left.toFloat(),
                            view.bottom.toFloat(),
                            view.right.toFloat(),
                            (view.bottom + normalHeight).toFloat(),
                            paint,
                        )
                    }
                }
            }
        } catch (e: Exception) {
            // Do nothing
        }
    }
}
