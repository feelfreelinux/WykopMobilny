package io.github.feelfreelinux.wykopmobilny.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R

abstract class SimpleBaseProgressAdapter<T : androidx.recyclerview.widget.RecyclerView.ViewHolder, A : Any> :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), BaseProgressAdapter<A> {

    companion object {
        const val ITEM_PROGRESS = 2001
    }

    private val dataset = arrayListOf<A?>()

    override val data: List<A>
        get() = dataset.filterNotNull()

    open val ITEM_TYPE = 2002

    abstract fun createViewHolder(parent: ViewGroup): T

    abstract fun bindHolder(holder: T, position: Int)

    override fun disableLoading() {
        if (dataset.isNotEmpty() && dataset.last() == null) {
            val size = dataset.size - 1
            dataset.removeAt(size)
            notifyItemRemoved(size)
        }
    }

    override fun addData(items: List<A>, shouldClearAdapter: Boolean) {
        if (shouldClearAdapter) {
            dataset.apply {
                clear()
                addAll(items)
                add(null)
            }
            notifyDataSetChanged()
        } else {
            val filteredItems = items.filter { !dataset.contains(it) }
            if (dataset.last() == null) {
                dataset.removeAt(dataset.size - 1)
            }
            dataset.addAll(filteredItems)
            dataset.add(null)
            notifyItemRangeInserted(dataset.size - filteredItems.size, filteredItems.size + 1)
        }
    }

    override fun getItemId(position: Int) = dataset[position]?.hashCode()?.toLong() ?: position.toLong()

    override fun getItemCount() = dataset.size

    override fun getItemViewType(position: Int): Int =
        when {
            dataset[position] == null -> ITEM_PROGRESS
            else -> ITEM_TYPE
        }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (dataset[position] != null) bindHolder(holder as T, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            ITEM_TYPE -> createViewHolder(parent)
            else -> ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.progress_item, parent, false))
        }

    class ProgressViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}
