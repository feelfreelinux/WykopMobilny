package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.CommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class EntryCommentAdapter @Inject constructor(val presenterFactory: CommentPresenterFactory,
                                              val userManagerApi: UserManagerApi,
                                              val settingsPreferencesApi: SettingsPreferencesApi) : SimpleBaseProgressAdapter<CommentViewHolder, EntryComment>() {
    override fun bindHolder(holder: CommentViewHolder, position: Int) {
        holder.bindView(data[position], true, -1)
    }

    override fun createViewHolder(parent: ViewGroup): CommentViewHolder =
            CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item, parent, false), {}, settingsPreferencesApi, userManagerApi, presenterFactory.create())

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}