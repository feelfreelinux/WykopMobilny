package io.github.feelfreelinux.wykopmobilny.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.helpers.EndlessScrollListener

abstract class EndlessProgressAdapter<T : androidx.recyclerview.widget.RecyclerView.ViewHolder, A : Any> :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_PROGRESS = 0
    }

    var loadNewDataListener: () -> Unit = {}
    internal val dataset = arrayListOf<A?>()
    private var isLoading = false

    open val data: List<A>
        get() = dataset.filterNotNull()

    abstract fun constructViewHolder(parent: ViewGroup, viewType: Int): T

    abstract fun bindHolder(holder: T, position: Int)

    override fun getItemId(position: Int) = dataset[position]?.hashCode()?.toLong() ?: position.toLong()

    // Attach EndlessScrollListener
    override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
            recyclerView.addOnScrollListener(EndlessScrollListener(recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager) {
                if (dataset.isNotEmpty() && dataset.last() == null) {
                    if (!isLoading) loadNewDataListener()
                    isLoading = true
                }
            })
        }
    }

    fun disableLoading() {
        if (dataset.isNotEmpty() && dataset.last() == null) {
            val size = dataset.size - 1
            dataset.removeAt(size)
            notifyItemRemoved(size)
        }
        isLoading = true
    }

    open fun addData(items: List<A>, shouldClearAdapter: Boolean) {
        if (shouldClearAdapter || dataset.isEmpty()) {
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
        isLoading = false
    }

    override fun getItemCount() = dataset.size

    abstract fun getViewType(position: Int): Int

    override fun getItemViewType(position: Int): Int =
        if (dataset[position] == null) ITEM_PROGRESS
        else getViewType(position)

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (dataset[position] != null) bindHolder(holder as T, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            ITEM_PROGRESS -> ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.progress_item, parent, false))
            else -> constructViewHolder(parent, viewType)
        }

    class ProgressViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}
