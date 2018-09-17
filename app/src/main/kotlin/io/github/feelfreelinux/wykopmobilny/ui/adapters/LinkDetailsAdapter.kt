package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BaseLinkCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkHeaderViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.TopLinkCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.fragments.link.LinkHeaderActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentViewListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class LinkDetailsAdapter @Inject constructor(val userManagerApi: UserManagerApi,
                                             val navigatorApi: NewNavigatorApi,
                                             val linkHandlerApi: WykopLinkHandlerApi,
                                             val settingsPreferencesApi: SettingsPreferencesApi) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    var highlightCommentId = -1
    lateinit var linkCommentViewListener: LinkCommentViewListener
    lateinit var linkCommentActionListener: LinkCommentActionListener
    lateinit var linkHeaderActionListener: LinkHeaderActionListener
    var link: Link? = null
    val commentsList : List<LinkComment>? get() = link?.comments?.filterNot { it.isParentCollapsed || (it.isBlocked && settingsPreferencesApi.hideBlacklistedViews) }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == LinkHeaderViewHolder.TYPE_HEADER) {
            link?.let { (holder as LinkHeaderViewHolder).bindView(link!!) }
        } else if (holder is BlockedViewHolder) {
            holder.bindView(commentsList!![position - 1])
        } else {
            val comment = commentsList!![position - 1]
            if (holder is TopLinkCommentViewHolder) {
                holder.bindView(comment, link!!.author?.nick == comment.author.nick, highlightCommentId)
                holder.containerView.tag = if (comment.childCommentCount > 0 && !comment.isCollapsed) RecyclableViewHolder.SEPARATOR_SMALL else RecyclableViewHolder.SEPARATOR_NORMAL
            } else if (holder is LinkCommentViewHolder) {
                val parent = commentsList!!.first { it.id == comment.parentId }
                val index = commentsList!!.subList(commentsList!!.indexOf(parent), position-1).size
                holder.bindView(comment, link!!.author?.nick == comment.author.nick, highlightCommentId)
                holder.itemView.tag = if (parent.childCommentCount == index) RecyclableViewHolder.SEPARATOR_NORMAL else RecyclableViewHolder.SEPARATOR_SMALL
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) LinkHeaderViewHolder.TYPE_HEADER
        else BaseLinkCommentViewHolder.getViewTypeForComment(commentsList!![position-1])
    }

    override fun getItemCount(): Int {
        commentsList?.let {
            return it.size + 1
        }
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return when (viewType) {
            LinkHeaderViewHolder.TYPE_HEADER -> LinkHeaderViewHolder.inflateView(parent, userManagerApi, navigatorApi, linkHandlerApi, linkHeaderActionListener)
            TopLinkCommentViewHolder.TYPE_TOP_EMBED, TopLinkCommentViewHolder.TYPE_TOP_NORMAL -> TopLinkCommentViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkHandlerApi, linkCommentActionListener, linkCommentViewListener)
            LinkCommentViewHolder.TYPE_EMBED, LinkCommentViewHolder.TYPE_NORMAL -> LinkCommentViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkHandlerApi, linkCommentActionListener, linkCommentViewListener)
            else -> BlockedViewHolder.inflateView(parent, { notifyItemChanged(it) })
        }
    }

    override fun onViewRecycled(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }

    fun updateLinkComment(comment : LinkComment) {
        val position = link!!.comments.indexOf(comment)
        link!!.comments[position] = comment
        notifyItemChanged(commentsList!!.indexOf(comment) + 1)
    }

    fun updateLinkHeader(link : Link) {
        this.link = link
        notifyItemChanged(0)
    }
}
