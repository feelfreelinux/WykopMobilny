package io.github.feelfreelinux.wykopmobilny.base

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R

const val ITEM_TYPE = 0
const val ITEM_PROGRESS = 1

abstract class BaseProgressAdapter<T : RecyclerView.ViewHolder, A : Any> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataset = arrayListOf<A?>()

    val data: List<A>
        get() = dataset.filterNotNull()

    fun disableLoading() {
        if (dataset.isNotEmpty() && dataset.last() == null) {
            val size = dataset.size - 1
            dataset.removeAt(size)
            notifyItemRemoved(size)
        }
    }

    fun addData(items: List<A>, shouldClearAdapter: Boolean) {
        if (shouldClearAdapter) {
            dataset.apply {
                clear()
                addAll(items)
                add(null)
            }
            notifyDataSetChanged()
        } else {
            if (dataset.last() == null) {
                dataset.removeAt(dataset.size - 1)
            }
            dataset.addAll(items)
            dataset.add(null)
            notifyItemRangeInserted(dataset.size - items.size, items.size + 1)
        }
    }


    override fun getItemCount() = dataset.size

    class ProgressViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int =
            if (dataset[position] == null) ITEM_PROGRESS
            else ITEM_TYPE

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (dataset[position] != null) bindHolder(holder as T, position)
    }

    abstract fun createViewHolder(parent: ViewGroup): T

    abstract fun bindHolder(holder: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE -> createViewHolder(parent)
            else -> ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.progress_item, parent, false))
        }
    }
}
