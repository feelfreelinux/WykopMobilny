package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class FeedAdapter @Inject constructor(
    val userManagerApi: UserManagerApi,
    val settingsPreferencesApi: SettingsPreferencesApi
) : SimpleBaseProgressAdapter<BlockedViewHolder, Entry>() {

    override val ITEM_TYPE = 0

    override fun bindHolder(holder: BlockedViewHolder, position: Int) =
        holder.bindView(data[position])

    override fun createViewHolder(parent: ViewGroup): BlockedViewHolder =
        BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }
}