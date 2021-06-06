package io.github.wykopmobilny.ui.adapters

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.ui.adapters.viewholders.BaseLinkCommentViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.BlockedViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.LinkCommentViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.LinkHeaderViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.wykopmobilny.ui.adapters.viewholders.TopLinkCommentViewHolder
import io.github.wykopmobilny.ui.fragments.link.LinkHeaderActionListener
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentViewListener
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi
import javax.inject.Inject

class LinkDetailsAdapter @Inject constructor(
    private val userManagerApi: UserManagerApi,
    private val navigatorApi: NewNavigatorApi,
    private val linkHandlerApi: WykopLinkHandlerApi,
    private val settingsPreferencesApi: SettingsPreferencesApi
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var link: Link? = null
    var highlightCommentId = -1
    lateinit var linkCommentViewListener: LinkCommentViewListener
    lateinit var linkCommentActionListener: LinkCommentActionListener
    lateinit var linkHeaderActionListener: LinkHeaderActionListener
    private val commentsList: List<LinkComment>?
        get() = link?.comments?.filterNot {
            it.isParentCollapsed || (it.isBlocked && settingsPreferencesApi.hideBlacklistedViews)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try { // Suppresing, need more information to reproduce this crash.
            if (holder.itemViewType == LinkHeaderViewHolder.TYPE_HEADER) {
                link?.let { (holder as LinkHeaderViewHolder).bindView(link!!) }
            } else if (holder is BlockedViewHolder) {
                holder.bindView(commentsList!![position - 1])
            } else {
                val comment = commentsList!![position - 1]
                if (holder is TopLinkCommentViewHolder) {
                    holder.bindView(comment, link!!.author?.nick == comment.author.nick, highlightCommentId)
                    holder.itemView.tag =
                        if (comment.childCommentCount > 0 && !comment.isCollapsed) {
                            RecyclableViewHolder.SEPARATOR_SMALL
                        } else {
                            RecyclableViewHolder.SEPARATOR_NORMAL
                        }
                } else if (holder is LinkCommentViewHolder) {
                    val parent = commentsList!!.first { it.id == comment.parentId }
                    val index = commentsList!!.subList(commentsList!!.indexOf(parent), position - 1).size
                    holder.bindView(comment, link!!.author?.nick == comment.author.nick, highlightCommentId)
                    holder.itemView.tag =
                        if (parent.childCommentCount == index) {
                            RecyclableViewHolder.SEPARATOR_NORMAL
                        } else {
                            RecyclableViewHolder.SEPARATOR_SMALL
                        }
                }
            }
        } catch (exception: Exception) {
            Log.w(this::class.simpleName, "Couldn't bind view holder", exception)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) LinkHeaderViewHolder.TYPE_HEADER
        else BaseLinkCommentViewHolder.getViewTypeForComment(commentsList!![position - 1])
    }

    override fun getItemCount(): Int {
        commentsList?.let {
            return it.size + 1
        }
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LinkHeaderViewHolder.TYPE_HEADER -> LinkHeaderViewHolder.inflateView(
                parent,
                userManagerApi,
                navigatorApi,
                linkHandlerApi,
                linkHeaderActionListener
            )
            TopLinkCommentViewHolder.TYPE_TOP_EMBED, TopLinkCommentViewHolder.TYPE_TOP_NORMAL -> TopLinkCommentViewHolder.inflateView(
                parent,
                viewType,
                userManagerApi,
                settingsPreferencesApi,
                navigatorApi,
                linkHandlerApi,
                linkCommentActionListener,
                linkCommentViewListener
            )
            LinkCommentViewHolder.TYPE_EMBED, LinkCommentViewHolder.TYPE_NORMAL -> LinkCommentViewHolder.inflateView(
                parent,
                viewType,
                userManagerApi,
                settingsPreferencesApi,
                navigatorApi,
                linkHandlerApi,
                linkCommentActionListener,
                linkCommentViewListener
            )
            else -> BlockedViewHolder.inflateView(parent) { notifyItemChanged(it) }
        }
    }

    fun updateLinkComment(comment: LinkComment) {
        val position = link!!.comments.indexOf(comment)
        link!!.comments[position] = comment
        notifyItemChanged(commentsList!!.indexOf(comment) + 1)
    }

    fun updateLinkHeader(link: Link) {
        this.link = link
        notifyItemChanged(0)
    }
}
