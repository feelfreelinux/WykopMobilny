package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.SimpleLinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class NoProgressLinkAdapter @Inject constructor(
    val userManagerApi: UserManagerApi,
    val settingsPreferencesApi: SettingsPreferencesApi,
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    // Required field, interacts with presenter. Otherwise will throw exception
    val items = mutableListOf<Link>()
    lateinit var linksActionListener: LinkActionListener

    override fun getItemViewType(position: Int): Int {
        return if (settingsPreferencesApi.linkSimpleList) {
            SimpleLinkViewHolder.getViewTypeForLink(items[position])
        } else {
            LinkViewHolder.getViewTypeForLink(items[position], settingsPreferencesApi)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            SimpleLinkViewHolder.TYPE_SIMPLE_LINK ->
                SimpleLinkViewHolder.inflateView(parent, userManagerApi, settingsPreferencesApi, navigatorApi, linksActionListener)
            SimpleLinkViewHolder.TYPE_BLOCKED, LinkViewHolder.TYPE_BLOCKED ->
                BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }
            else -> LinkViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linksActionListener)
        }
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LinkViewHolder -> holder.bindView(items[position])
            is SimpleLinkViewHolder -> holder.bindView(items[position])
            is BlockedViewHolder -> holder.bindView(items[position])
        }
    }

    override fun getItemCount() = items.size

    fun updateLink(link: Link) {
        val position = items.indexOf(link)
        items[position] = link
        notifyItemChanged(position)
    }
}