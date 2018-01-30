package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkCommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkHeaderViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.LinkPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class LinkDetailsAdapter @Inject constructor(val presenterFactory: LinkCommentPresenterFactory,
                                             val linkPresenterFactory: LinkPresenterFactory,
                                             val userManagerApi: UserManagerApi,
                                             val settingsPreferencesApi: SettingsPreferencesApi) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val HEADER_HOLDER = 0
        private const val COMMENT_HOLDER = 1
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
            (holder as LinkCommentViewHolder).bindView(comment, link!!.author?.nick == comment.author.nick, highlightCommentId)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER_HOLDER
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
            else -> LinkCommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_comment_list_item, parent, false), onReplyClickedListener, collapseListener, presenterFactory.create(), userManagerApi, settingsPreferencesApi)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}
