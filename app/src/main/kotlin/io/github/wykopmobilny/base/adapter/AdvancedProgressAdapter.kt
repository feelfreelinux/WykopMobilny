package io.github.wykopmobilny.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.databinding.ProgressItemBinding
import io.github.wykopmobilny.utils.layoutInflater

abstract class AdvancedProgressAdapter<A : Any> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseProgressAdapter<A> {

    companion object {
        const val VIEWTYPE_PROGRESS = 0 // For any other viewtype use value greater than 0
    }

    internal val dataset = arrayListOf<A?>()

    override val data: List<A>
        get() = dataset.filterNotNull()

    abstract fun createViewHolder(viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindHolder(holder: RecyclerView.ViewHolder, position: Int)

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
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (dataset[position] != null) bindHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == EndlessProgressAdapter.ITEM_PROGRESS) {
            ProgressViewHolder(ProgressItemBinding.inflate(parent.layoutInflater, parent, false))
        } else {
            createViewHolder(parent, viewType)
        }

    class ProgressViewHolder(val binding: ProgressItemBinding) : RecyclerView.ViewHolder(binding.root)
}
