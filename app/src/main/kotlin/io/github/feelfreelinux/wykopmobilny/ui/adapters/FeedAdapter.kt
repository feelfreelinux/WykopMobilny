package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class FeedAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi, val entryPresenterFactory: EntryPresenterFactory) : SimpleBaseProgressAdapter<EntryViewHolder, Entry>() {
    override fun bindHolder(holder: EntryViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    override fun createViewHolder(parent: ViewGroup): EntryViewHolder =
            EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false), userManagerApi, null, settingsPreferencesApi, entryPresenterFactory.create())

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}