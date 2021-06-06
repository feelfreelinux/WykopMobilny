package io.github.wykopmobilny.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.databinding.ProgressItemBinding
import io.github.wykopmobilny.utils.layoutInflater

abstract class SimpleBaseProgressAdapter<T : RecyclerView.ViewHolder, A : Any> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseProgressAdapter<A> {

    companion object {
        const val ITEM_PROGRESS = 2001
        const val DEFAULT_PROGRES_ITEM = 2002
    }

    private val dataset = arrayListOf<A?>()

    override val data: List<A>
        get() = dataset.filterNotNull()

    open val itemType = DEFAULT_PROGRES_ITEM

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
        if (dataset[position] == null) {
            ITEM_PROGRESS
        } else {
            itemType
        }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (dataset[position] != null) bindHolder(holder as T, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            itemType -> createViewHolder(parent)
            ITEM_PROGRESS -> ProgressViewHolder(ProgressItemBinding.inflate(parent.layoutInflater, parent, false))
            else -> error("Not supported")
        }

    class ProgressViewHolder(val binding: ProgressItemBinding) : RecyclerView.ViewHolder(binding.root)
}
