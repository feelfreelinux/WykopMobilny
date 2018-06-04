package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.AdvancedProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.*
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.LinkItemPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.LinkItemWidget
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.SimpleItemWidget
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class EntryLinkAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi : SettingsPreferencesApi,
                                           val entryPresenterFactory: EntryPresenterFactory,
                                           val linkItemPresenterFactory: LinkItemPresenterFactory) : AdvancedProgressAdapter<EntryLink>() {
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

    override fun createViewHolder(viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder =
            BlockedViewHolder.inflateView(parent, { notifyItemChanged(it) })

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]!!
        if (item.entry != null) {
            (holder as BlockedViewHolder).bindView(item.entry!!)
        } else if (item.link != null) {
            if (holder is SimpleLinkViewHolder) holder.bindView(item.link!!)
            else (holder as? LinkViewHolder)?.bindView(item.link!!)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
    }

}
