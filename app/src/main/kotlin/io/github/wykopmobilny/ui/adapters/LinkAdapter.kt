package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.base.adapter.AdvancedProgressAdapter
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.SimpleLinkViewHolder
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import javax.inject.Inject

class LinkAdapter @Inject constructor(val settingsPreferencesApi: SettingsPreferencesApi) : AdvancedProgressAdapter<Link>() {

    companion object {
        private const val LINK_VIEWTYPE = 1
        private const val SIMPLE_LINK_VIEWTYPE = 3
    }

    override fun getItemViewType(position: Int): Int =
        when {
            dataset[position] == null -> VIEWTYPE_PROGRESS
            settingsPreferencesApi.linkSimpleList -> SIMPLE_LINK_VIEWTYPE
            else -> LINK_VIEWTYPE
        }

    override fun createViewHolder(viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder =
        BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]!!
        when (holder) {
            is SimpleLinkViewHolder -> holder.bindView(item)
            is LinkViewHolder -> holder.bindView(item)
        }
    }
}
