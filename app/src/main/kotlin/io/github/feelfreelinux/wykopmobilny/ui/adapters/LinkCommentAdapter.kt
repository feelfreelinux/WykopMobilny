package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.ProfileLinkCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class LinkCommentAdapter @Inject constructor(val presenterFactory: LinkCommentPresenterFactory,
                                             val userManagerApi: UserManagerApi,
                                             val settingsPreferencesApi: SettingsPreferencesApi) : SimpleBaseProgressAdapter<ProfileLinkCommentViewHolder, LinkComment>() {
    override fun bindHolder(holder: ProfileLinkCommentViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    override fun createViewHolder(parent: ViewGroup): ProfileLinkCommentViewHolder =
            ProfileLinkCommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_top_comment_list_item, parent, false), presenterFactory.create(), userManagerApi, settingsPreferencesApi)

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}