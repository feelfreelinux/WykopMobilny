package io.github.feelfreelinux.wykopmobilny.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R

abstract class AdvancedProgressAdapter<A : Any> : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseProgressAdapter<A> {
    companion object {
        val VIEWTYPE_PROGRESS = 0 // For any other viewtype use value greater than 0
    }

    internal val dataset = arrayListOf<A?>()

    override val data: List<A>
        get() = dataset.filterNotNull()

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

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (dataset[position] != null) bindHolder(holder!!, position)
    }

    abstract fun createViewHolder(viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindHolder(holder: RecyclerView.ViewHolder, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEWTYPE_PROGRESS -> ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.progress_item, parent, false))
            else -> createViewHolder(viewType, parent)
        }
    }
}
