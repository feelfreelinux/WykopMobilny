package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.*
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentViewListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class EntryAdapter @Inject constructor(val userManagerApi: UserManagerApi, val settingsPreferencesApi: SettingsPreferencesApi, val navigatorApi: NewNavigatorApi, val linkHandlerApi: WykopLinkHandlerApi) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var entryActionListener : EntryActionListener
    lateinit var commentActionListener : EntryCommentActionListener
    lateinit var commentViewListener: EntryCommentViewListener
    var replyListener : EntryListener = {
        commentViewListener.addReply(it.author)
    }

    var entry: Entry? = null
    var commentId : Int? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EntryViewHolder -> {
                holder.bindView(entry!!)
            }
            is EntryCommentViewHolder -> {
                val comment = entry!!.comments[position - 1]
                holder.bindView(comment, entry?.author, commentId ?: 0)
            }
            is BlockedViewHolder -> {
                if (position == 0) {
                    holder.bindView(entry!!)
                } else {
                    holder.bindView(entry!!.comments[position - 1])
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) EntryViewHolder.getViewTypeForEntry(entry!!)
        else EntryCommentViewHolder.getViewTypeForEntryComment(entry!!.comments[position - 1])
    }

    override fun getItemCount(): Int {
        entry?.comments?.let {
            return it.size + 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EntryCommentViewHolder.TYPE_NORMAL, EntryCommentViewHolder.TYPE_BLOCKED, EntryCommentViewHolder.TYPE_EMBED -> EntryCommentViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkHandlerApi, commentActionListener, commentViewListener, false)
            else -> EntryViewHolder.inflateView(parent, viewType, userManagerApi, settingsPreferencesApi, navigatorApi, linkHandlerApi, entryActionListener, replyListener)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }

    fun updateEntry(entry : Entry) {
        this.entry = entry
        notifyItemChanged(0)
    }

    fun updateComment(comment : EntryComment) {
        val position = entry!!.comments.indexOf(comment)
        entry!!.comments[position] = comment
        notifyItemChanged(position + 1)
    }
}
