package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
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

class EntryLinksAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi, val navigatorApi: NewNavigatorApi, val linkHandlerApi: WykopLinkHandlerApi) : EndlessProgressAdapter<androidx.recyclerview.widget.RecyclerView.ViewHolder, EntryLink>() {
    // Required field, interacts with presenter. Otherwise will throw exception
    lateinit var entryActionListener : EntryActionListener
    lateinit var linkActionListener : LinkActionListener

    override fun getViewType(position: Int): Int {
        val entryLink = dataset[position]
        return if (entryLink?.DATA_TYPE == EntryLink.TYPE_ENTRY) {
            EntryViewHolder.getViewTypeForEntry(entryLink.entry!!)
        } else {
            LinkViewHolder.getViewTypeForLink(entryLink!!.link!!, settingsPreferencesApi)
        }
    }

    override fun addData(items: List<EntryLink>, shouldClearAdapter: Boolean) {
        super.addData(items.filterNot { settingsPreferencesApi.hideBlacklistedViews && if (it.entry != null) it.entry!!.isBlocked else it.link!!.isBlocked }, shouldClearAdapter)
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            LinkViewHolder.TYPE_IMAGE, LinkViewHolder.TYPE_NOIMAGE ->
                    LinkViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkActionListener)
            EntryViewHolder.TYPE_BLOCKED, LinkViewHolder.TYPE_BLOCKED ->
                BlockedViewHolder.inflateView(parent, { notifyItemChanged(it) })
            else -> EntryViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkHandlerApi, entryActionListener, null)
        }
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EntryViewHolder -> {
                dataset[position]?.entry?.let { holder.bindView(it) }
            }
            is LinkViewHolder -> dataset[position]?.link?.let { holder.bindView(it) }
            is BlockedViewHolder -> {
                val data = dataset[position]
                data?.link?.let {
                    holder.bindView(it)
                }

                data?.entry?.let {
                    holder.bindView(it)
                }
            }
            is SimpleLinkViewHolder -> dataset[position]!!.link?.let { holder.bindView(it) }
        }
    }

    fun updateEntry(entry : Entry) {
        val position = dataset.indexOfFirst { it!!.entry?.id == entry.id }
        dataset[position]!!.entry = entry
        notifyItemChanged(position)
    }

    fun updateLink(link : Link) {
        val position = dataset.indexOfFirst { it!!.link?.id == link.id }
        dataset[position]!!.link = link
        notifyItemChanged(position)
    }
}