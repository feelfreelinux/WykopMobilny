package io.github.feelfreelinux.wykopmobilny.base

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.printout

const val ITEM_TYPE = 0
const val ITEM_PROGRESS = 1

abstract class BaseProgressAdapter<T : RecyclerView.ViewHolder, A : Any> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val dataset = ArrayList<A?>()

    var isLoading : Boolean
        get() = if (dataset.size > 0) dataset.last() == null else false
        set(value) = showProgress(value)

    private fun showProgress(shouldShow : Boolean) {
        if(shouldShow) {
            if (!isLoading) {
                // Show progress footer
                dataset.add(null)
                notifyItemInserted(dataset.size)
            }
        }
        else if(isLoading) dataset.removeAt(dataset.size - 1)

    }

    override fun getItemCount() = dataset.size

    class ProgressViewHolder(val view : View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int =
        if (dataset[position] == null) ITEM_PROGRESS
        else ITEM_TYPE

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (dataset[position] != null) bindHolder(holder as T, position)
    }

    abstract fun createViewHolder(parent: ViewGroup) : T

    abstract fun bindHolder(holder : T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_TYPE -> createViewHolder(parent)
            else      -> ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.progress_item, parent, false))
        }
    }
}
