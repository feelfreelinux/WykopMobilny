package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.*
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.LinkPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class LinkDetailsAdapter @Inject constructor(val presenterFactory: LinkCommentPresenterFactory,
                                             val linkPresenterFactory: LinkPresenterFactory,
                                             val userManagerApi: UserManagerApi,
                                             val settingsPreferencesApi: SettingsPreferencesApi) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val HEADER_HOLDER = 4
        val TOP_COMMENT_HOLDER = 5
        val COMMENT_HOLDER = 6
        val COMMENT_TYPE_LARGE = "LARGE_TYPE"
        val COMMENT_TYPE_NORMAL = "NORMAL_TYPE"
        val COMMENT_TYPE = 156
    }

    var highlightCommentId = -1
    lateinit var collapseListener : (Boolean, Int) -> Unit

    var link: Link? = null
    val commentsList : List<LinkComment>? get() = link?.comments?.filterNot { it.isParentCollapsed }
    var onReplyClickedListener : (LinkComment) -> Unit = {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == HEADER_HOLDER) {
            link?.let { (holder as LinkHeaderViewHolder).bindView(link!!) }
        } else {
            val comment = commentsList!![position - 1]
            if (holder.itemViewType == TOP_COMMENT_HOLDER) {
                (holder as TopLinkCommentViewHolder).bindView(comment, link!!.author?.nick == comment.author.nick, highlightCommentId)
                holder.itemView.tag = if (comment.childCommentCount > 0) COMMENT_TYPE_LARGE else COMMENT_TYPE_NORMAL
            } else {
                val parent = commentsList!!.first { it.id == comment.parentId }
                val index = commentsList!!.subList(commentsList!!.indexOf(parent), position-1).size
                (holder as LinkCommentViewHolder).bindView(comment, link!!.author?.nick == comment.author.nick, highlightCommentId)
                holder.itemView.tag = if (parent.childCommentCount == index) COMMENT_TYPE_LARGE else COMMENT_TYPE_NORMAL
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER_HOLDER
        else if (commentsList!![position-1].parentId == commentsList!![position-1].id) TOP_COMMENT_HOLDER
        else COMMENT_HOLDER
    }

    override fun getItemCount(): Int {
        commentsList?.let {
            return it.size + 1
        }
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_HOLDER -> LinkHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_details_header_list_item, parent, false), linkPresenterFactory.create(), userManagerApi)
            TOP_COMMENT_HOLDER -> TopLinkCommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_top_comment_list_item, parent, false), onReplyClickedListener, collapseListener, presenterFactory.create(), userManagerApi, settingsPreferencesApi)
            else -> LinkCommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_comment_list_item, parent, false), onReplyClickedListener, presenterFactory.create(), userManagerApi, settingsPreferencesApi)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}
