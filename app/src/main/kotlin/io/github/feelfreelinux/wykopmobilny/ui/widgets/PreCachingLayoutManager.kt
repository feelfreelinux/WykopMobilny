package io.github.feelfreelinux.wykopmobilny.ui.widgets


import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by carlo_000 on 10/12/2015.
 */
class PreCachingLayoutManager : androidx.recyclerview.widget.LinearLayoutManager {
    private val context: Context
    private var extraLayoutSpace = -1
    private val DEFAULT_EXTRA_LAYOUT_SPACE = 200

    constructor(context: Context) : super(context) {

        this.context = context
    }

    override fun onLayoutChildren(recycler: androidx.recyclerview.widget.RecyclerView.Recycler?, state: androidx.recyclerview.widget.RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
        }

    }

    constructor(context: Context, extraLayoutSpace: Int) : super(context) {
        this.context = context
        this.extraLayoutSpace = extraLayoutSpace

    }

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {
        this.context = context
    }

    fun setExtraLayoutSpace(extraLayoutSpace: Int) {
        this.extraLayoutSpace = extraLayoutSpace
    }

    override fun getExtraLayoutSpace(state: androidx.recyclerview.widget.RecyclerView.State): Int {
        if (extraLayoutSpace > 0) {
            return extraLayoutSpace;
        }
        return DEFAULT_EXTRA_LAYOUT_SPACE
    }
}