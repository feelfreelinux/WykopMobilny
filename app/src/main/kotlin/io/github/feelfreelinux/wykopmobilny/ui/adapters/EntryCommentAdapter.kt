package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class EntryCommentAdapter @Inject constructor(val presenterFactory: CommentPresenterFactory,
                                              val userManagerApi: UserManagerApi,
                                              val settingsPreferencesApi: SettingsPreferencesApi) : SimpleBaseProgressAdapter<BlockedViewHolder, EntryComment>() {
    override fun bindHolder(holder: BlockedViewHolder, position: Int) {
    }

    override fun createViewHolder(parent: ViewGroup): BlockedViewHolder =
           BlockedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false))

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}