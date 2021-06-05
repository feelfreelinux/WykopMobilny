package io.github.feelfreelinux.wykopmobilny.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.feelfreelinux.wykopmobilny.databinding.ProgressItemBinding
import io.github.feelfreelinux.wykopmobilny.ui.helpers.EndlessScrollListener
import io.github.feelfreelinux.wykopmobilny.utils.layoutInflater

abstract class EndlessProgressAdapter<T : RecyclerView.ViewHolder, A : Any> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
            recyclerView.addOnScrollListener(
                EndlessScrollListener(recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager) {
                    if (dataset.isNotEmpty() && dataset.last() == null) {
                        if (!isLoading) loadNewDataListener()
                        isLoading = true
                    }
                }
            )
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
        if (dataset[position] == null) {
            ITEM_PROGRESS
        } else {
            getViewType(position)
        }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (dataset[position] != null) bindHolder(holder as T, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == ITEM_PROGRESS) {
            ProgressViewHolder(ProgressItemBinding.inflate(parent.layoutInflater, parent, false))
        } else {
            createViewHolder(parent, viewType)
        }

    class ProgressViewHolder(val binding: ProgressItemBinding) : RecyclerView.ViewHolder(binding.root)
}
