package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.EntryListener
import io.github.wykopmobilny.ui.adapters.viewholders.EntryViewHolder
import io.github.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi
import javax.inject.Inject

class EntriesAdapter @Inject constructor(
    val userManagerApi: UserManagerApi,
    val settingsPreferencesApi: SettingsPreferencesApi,
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : EndlessProgressAdapter<androidx.recyclerview.widget.RecyclerView.ViewHolder, Entry>() {

    // Required field, interacts with presenter. Otherwise will throw exception
    lateinit var entryActionListener: EntryActionListener

    var replyListener: EntryListener? = null

    override fun getViewType(position: Int): Int {
        val entry = dataset[position]!!
        return EntryViewHolder.getViewTypeForEntry(entry)
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return if (viewType == EntryViewHolder.TYPE_BLOCKED) {
            BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }
        } else {
            EntryViewHolder.inflateView(
                parent,
                viewType,
                userManagerApi,
                settingsPreferencesApi,
                navigatorApi,
                linkHandlerApi,
                entryActionListener,
                replyListener
            )
        }
    }

    override fun addData(items: List<Entry>, shouldClearAdapter: Boolean) {
        super.addData(items.filterNot { settingsPreferencesApi.hideBlacklistedViews && it.isBlocked }, shouldClearAdapter)
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (holder is EntryViewHolder) {
            holder.bindView(dataset[position]!!)
        } else if (holder is BlockedViewHolder) {
            holder.bindView(dataset[position]!!)
        }
    }

    fun updateEntry(entry: Entry) {
        val position = dataset.indexOf(entry)
        dataset[position] = entry
        notifyItemChanged(position)
    }
}
