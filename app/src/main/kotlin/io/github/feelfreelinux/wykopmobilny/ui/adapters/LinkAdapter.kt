package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.base.adapter.AdvancedProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.SimpleLinkViewHolder
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import javax.inject.Inject

class LinkAdapter @Inject constructor(val settingsPreferencesApi: SettingsPreferencesApi) : AdvancedProgressAdapter<Link>() {
    companion object {
        const val LINK_VIEWTYPE = 1
        const val SIMPLE_LINK_VIEWTYPE = 3
    }

    override fun getItemViewType(position: Int): Int = when {
        dataset[position] == null -> AdvancedProgressAdapter.VIEWTYPE_PROGRESS
        else -> if (settingsPreferencesApi.linkSimpleList) SIMPLE_LINK_VIEWTYPE else LINK_VIEWTYPE
    }

    override fun createViewHolder(viewType: Int, parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder =
            BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]!!
        if (holder is SimpleLinkViewHolder) holder.bindView(item)
        else (holder as? LinkViewHolder)?.bindView(item)
    }

    override fun onViewRecycled(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }

}
