package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.storage.api.LinksPreferencesApi
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.SimpleLinkViewHolder
import io.github.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class LinksAdapter @Inject constructor(
    private val userManagerApi: UserManagerApi,
    private val settingsPreferencesApi: SettingsPreferencesApi,
    private val navigatorApi: NewNavigatorApi,
    private val linksPreferences: LinksPreferencesApi,
) : EndlessProgressAdapter<androidx.recyclerview.widget.RecyclerView.ViewHolder, Link>() {

    // Required field, interacts with presenter. Otherwise will throw exception
    lateinit var linksActionListener: LinkActionListener

    override fun getViewType(position: Int): Int {
        return if (settingsPreferencesApi.linkSimpleList) {
            SimpleLinkViewHolder.getViewTypeForLink(dataset[position]!!)
        } else {
            LinkViewHolder.getViewTypeForLink(dataset[position]!!, settingsPreferencesApi)
        }
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            SimpleLinkViewHolder.TYPE_SIMPLE_LINK ->
                SimpleLinkViewHolder.inflateView(
                    parent = parent,
                    userManagerApi = userManagerApi,
                    settingsPreferencesApi = settingsPreferencesApi,
                    navigatorApi = navigatorApi,
                    linkActionListener = linksActionListener,
                    linksPreferences = linksPreferences,
                )
            SimpleLinkViewHolder.TYPE_BLOCKED, LinkViewHolder.TYPE_BLOCKED ->
                BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }
            else -> LinkViewHolder.inflateView(
                parent = parent,
                viewType = viewType,
                userManagerApi = userManagerApi,
                settingsPreferencesApi = settingsPreferencesApi,
                navigatorApi = navigatorApi,
                linkActionListener = linksActionListener,
                linksPreferences = linksPreferences,
            )
        }
    }

    override fun addData(items: List<Link>, shouldClearAdapter: Boolean) {
        super.addData(items.filterNot { settingsPreferencesApi.hideBlacklistedViews && it.isBlocked }, shouldClearAdapter)
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LinkViewHolder -> holder.bindView(dataset[position]!!)
            is SimpleLinkViewHolder -> holder.bindView(dataset[position]!!)
            is BlockedViewHolder -> holder.bindView(dataset[position]!!)
        }
    }

    fun updateLink(link: Link) {
        val position = dataset.indexOf(link)
        dataset[position] = link
        notifyItemChanged(position)
    }
}
