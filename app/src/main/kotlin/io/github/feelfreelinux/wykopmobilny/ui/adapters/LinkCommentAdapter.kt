package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BaseLinkCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.TopLinkCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class LinkCommentAdapter @Inject constructor(
    val userManagerApi: UserManagerApi,
    val settingsPreferencesApi: SettingsPreferencesApi,
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : EndlessProgressAdapter<androidx.recyclerview.widget.RecyclerView.ViewHolder, LinkComment>() {

    // Required field, interacts with presenter. Otherwise will throw exception
    lateinit var linkCommentActionListener: LinkCommentActionListener

    override fun getViewType(position: Int) = BaseLinkCommentViewHolder.getViewTypeForComment(data[position], true)

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            TopLinkCommentViewHolder.TYPE_TOP_EMBED, TopLinkCommentViewHolder.TYPE_TOP_NORMAL ->
                TopLinkCommentViewHolder.inflateView(
                    parent,
                    viewType,
                    userManagerApi,
                    settingsPreferencesApi,
                    navigatorApi,
                    linkHandlerApi,
                    linkCommentActionListener,
                    null
                )
            else ->
                BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }
        }
    }

    override fun addData(items: List<LinkComment>, shouldClearAdapter: Boolean) {
        super.addData(items.filterNot { settingsPreferencesApi.hideBlacklistedViews && it.isBlocked }, shouldClearAdapter)
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TopLinkCommentViewHolder -> holder.bindView(data[position], false)
            is BlockedViewHolder -> holder.bindView(data[position])
        }
    }

    fun updateComment(comment: LinkComment) {
        val position = data.indexOf(comment)
        dataset[position] = comment
        notifyItemChanged(position)
    }
}
