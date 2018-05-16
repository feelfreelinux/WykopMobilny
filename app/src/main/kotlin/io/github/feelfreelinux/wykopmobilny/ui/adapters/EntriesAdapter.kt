package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedEntryViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.EntryListener
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class EntriesAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi, val navigatorApi: NewNavigatorApi, val linkHandlerApi: WykopLinkHandlerApi) : EndlessProgressAdapter<RecyclerView.ViewHolder, Entry>() {
    // Required field, interacts with presenter. Otherwise will throw exception
    lateinit var entryActionListener : EntryActionListener

    var replyListener : EntryListener? = null

    override fun getViewType(position: Int): Int {
        val entry = data[position]
        return if (entry.isBlocked) EntryViewHolder.TYPE_BLOCKED
        else if (entry.embed != null && entry.survey == null) EntryViewHolder.TYPE_EMBED_SURVEY
        else if (entry.embed == null && entry.survey != null) EntryViewHolder.TYPE_SURVEY
        else if (entry.embed != null && entry.survey == null) EntryViewHolder.TYPE_EMBED
        else EntryViewHolder.TYPE_NORMAL
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == EntryViewHolder.TYPE_BLOCKED) {
            BlockedEntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.blocked_entry_view, parent, false))
        } else {
            EntryViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkHandlerApi, entryActionListener, replyListener)
        }
    }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EntryViewHolder) {
            holder.bindView(data[position])
        } else if (holder is BlockedEntryViewHolder) {
            holder.bindView(data[position])
        }
    }

    fun updateEntry(entry : Entry) {
        val position = data.indexOf(entry)
        dataset[position] = entry
        notifyItemChanged(position)
    }
}