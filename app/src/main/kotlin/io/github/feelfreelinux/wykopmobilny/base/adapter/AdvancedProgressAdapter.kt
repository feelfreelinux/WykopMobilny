package io.github.feelfreelinux.wykopmobilny.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R

abstract class AdvancedProgressAdapter<A : Any> :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), BaseProgressAdapter<A> {

    companion object {
        const val VIEWTYPE_PROGRESS = 0 // For any other viewtype use value greater than 0
    }

    internal val dataset = arrayListOf<A?>()

    override val data: List<A>
        get() = dataset.filterNotNull()

    abstract fun createViewHolder(viewType: Int, parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder

    abstract fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int)

    override fun getItemId(position: Int) =
        dataset[position]?.hashCode()?.toLong() ?: position.toLong()

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

    override fun getItemCount() = dataset.size

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (dataset[position] != null) bindHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            VIEWTYPE_PROGRESS -> ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.progress_item, parent, false))
            else -> createViewHolder(viewType, parent)
        }

    class ProgressViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}
