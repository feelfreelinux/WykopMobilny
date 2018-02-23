package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.AdvancedProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.*
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryWidget
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class EntryLinkAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi : SettingsPreferencesApi, val entryPresenterFactory: EntryPresenterFactory) : AdvancedProgressAdapter<EntryLink>() {
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
            when (viewType) {
                ENTRY_VIEWTYPE -> EntryViewHolder(EntryWidget.createView(parent.context), userManagerApi, null, settingsPreferencesApi, entryPresenterFactory.create())
                LINK_VIEWTYPE -> LinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_layout, parent, false), settingsPreferencesApi)
                else -> SimpleLinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.simple_link_layout, parent, false), settingsPreferencesApi)
            }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]!!
        if (item.entry != null) {
            (holder as EntryViewHolder).bindView(item.entry, true)
        } else if (item.link != null) {
            (holder as BaseLinkViewHolder).bindView(item.link)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }

}
