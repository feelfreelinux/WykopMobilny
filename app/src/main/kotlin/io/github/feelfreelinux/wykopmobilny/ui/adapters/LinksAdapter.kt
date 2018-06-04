package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.SimpleLinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class LinksAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi, val navigatorApi: NewNavigatorApi, val linkHandlerApi: WykopLinkHandlerApi) : EndlessProgressAdapter<RecyclerView.ViewHolder, Link>() {
    // Required field, interacts with presenter. Otherwise will throw exception
    lateinit var linksActionListener: LinkActionListener

    override fun getViewType(position: Int): Int {
        return if (settingsPreferencesApi.linkSimpleList) {
            SimpleLinkViewHolder.getViewTypeForLink(data[position])
        } else {
            LinkViewHolder.getViewTypeForLink(data[position], settingsPreferencesApi)
        }
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SimpleLinkViewHolder.TYPE_SIMPLE_LINK ->
                SimpleLinkViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linksActionListener)
            SimpleLinkViewHolder.TYPE_BLOCKED, LinkViewHolder.TYPE_BLOCKED ->
                BlockedViewHolder.inflateView(parent, { notifyItemChanged(it) })
            else -> LinkViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linksActionListener)
        }
    }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LinkViewHolder -> holder.bindView(data[position])
            is SimpleLinkViewHolder -> holder.bindView(data[position])
            is BlockedViewHolder -> {}
        }
    }

    fun updateLink(link : Link) {
        val position = data.indexOf(link)
        dataset[position] = link
        notifyItemChanged(position)
    }
}