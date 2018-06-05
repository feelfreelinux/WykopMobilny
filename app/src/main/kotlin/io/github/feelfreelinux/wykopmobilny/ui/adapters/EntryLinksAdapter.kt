package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.*
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class EntryLinksAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi, val navigatorApi: NewNavigatorApi, val linkHandlerApi: WykopLinkHandlerApi) : EndlessProgressAdapter<RecyclerView.ViewHolder, EntryLink>() {
    // Required field, interacts with presenter. Otherwise will throw exception
    lateinit var entryActionListener : EntryActionListener
    lateinit var linkActionListener : LinkActionListener

    override fun getViewType(position: Int): Int {
        val entryLink = data[position]
        return if (entryLink.DATA_TYPE == EntryLink.TYPE_ENTRY) {
            EntryViewHolder.getViewTypeForEntry(entryLink.entry!!)
        } else {
            LinkViewHolder.getViewTypeForLink(entryLink.link!!, settingsPreferencesApi)
        }
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LinkViewHolder.TYPE_IMAGE, LinkViewHolder.TYPE_NOIMAGE ->
                    LinkViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkActionListener)
            EntryViewHolder.TYPE_BLOCKED, LinkViewHolder.TYPE_BLOCKED ->
                BlockedViewHolder.inflateView(parent, { notifyItemChanged(it) })
            else -> EntryViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkHandlerApi, entryActionListener, null)
        }
    }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EntryViewHolder -> holder.bindView(data[position].entry!!)
            is LinkViewHolder -> holder.bindView(data[position].link!!)
            is BlockedViewHolder -> {
                val data = data[position]
                data.link?.let {
                    holder.bindView(data.link!!)
                }

                data.entry?.let {
                    holder.bindView(data.entry!!)
                }
            }
            is SimpleLinkViewHolder -> holder.bindView(data[position].link!!)
        }
    }

    fun updateEntry(entry : Entry) {
        val position = data.indexOfFirst { it.entry?.id == entry.id }
        data[position].entry = entry
        notifyItemChanged(position)
    }

    fun updateLink(link : Link) {
        val position = data.indexOfFirst { it.link?.id == link.id }
        data[position].link = link
        notifyItemChanged(position)
    }
}