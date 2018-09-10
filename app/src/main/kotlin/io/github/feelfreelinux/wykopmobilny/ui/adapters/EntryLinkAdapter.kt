package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.base.adapter.AdvancedProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.*
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class EntryLinkAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi : SettingsPreferencesApi) : AdvancedProgressAdapter<EntryLink>() {
    companion object {
        val ENTRY_VIEWTYPE = 1
        val LINK_VIEWTYPE = 2
        val SIMPLE_LINK_VIEWTYPE = 3
    }

    override fun getItemViewType(position: Int): Int = when {
        dataset[position] == null -> AdvancedProgressAdapter.VIEWTYPE_PROGRESS
        dataset[position]!!.entry != null -> ENTRY_VIEWTYPE
        else -> if (settingsPreferencesApi.linkSimpleList) SIMPLE_LINK_VIEWTYPE else LINK_VIEWTYPE
    }

    override fun createViewHolder(viewType: Int, parent: ViewGroup): androidx.recyclerview.widget.RecyclerView.ViewHolder =
            BlockedViewHolder.inflateView(parent, { notifyItemChanged(it) })

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]!!
        if (item.entry != null) {
            (holder as BlockedViewHolder).bindView(item.entry!!)
        } else if (item.link != null) {
            if (holder is SimpleLinkViewHolder) holder.bindView(item.link!!)
            else (holder as? LinkViewHolder)?.bindView(item.link!!)
        }
    }
}
