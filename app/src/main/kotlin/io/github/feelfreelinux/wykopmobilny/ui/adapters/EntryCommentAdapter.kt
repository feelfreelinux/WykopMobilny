package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.EntryCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class EntryCommentAdapter @Inject constructor(
    val userManagerApi: UserManagerApi,
    val settingsPreferencesApi: SettingsPreferencesApi,
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : EndlessProgressAdapter<RecyclerView.ViewHolder, EntryComment>() {
    // Required field, interacts with presenter. Otherwise will throw exception
    lateinit var entryCommentActionListener: EntryCommentActionListener

    override fun getViewType(position: Int) =
        EntryCommentViewHolder.getViewTypeForEntryComment(dataset[position]!!)

    override fun addData(items: List<EntryComment>, shouldClearAdapter: Boolean) {
        super.addData(items.filterNot { settingsPreferencesApi.hideBlacklistedViews && it.isBlocked }, shouldClearAdapter)
    }

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            EntryCommentViewHolder.TYPE_EMBED, EntryCommentViewHolder.TYPE_NORMAL ->
                EntryCommentViewHolder.inflateView(
                    parent,
                    viewType,
                    userManagerApi,
                    settingsPreferencesApi,
                    navigatorApi,
                    linkHandlerApi,
                    entryCommentActionListener,
                    null,
                    true
                )
            else -> BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }
        }
    }

    override fun bindHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EntryCommentViewHolder -> holder.bindView(dataset[position]!!, null)
            is BlockedViewHolder -> {
                holder.bindView(dataset[position]!!)
            }
        }
    }

    fun updateComment(comment: EntryComment) {
        val position = dataset.indexOf(comment)
        dataset[position] = comment
        notifyItemChanged(position)
    }
}