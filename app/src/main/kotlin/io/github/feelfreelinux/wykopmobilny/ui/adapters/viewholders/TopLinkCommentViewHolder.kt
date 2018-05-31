package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentViewListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.MinusVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.PlusVoteButton
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.top_link_comment_layout.*

class TopLinkCommentViewHolder(override val containerView: View,
                             userManagerApi: UserManagerApi,
                             settingsPreferencesApi: SettingsPreferencesApi,
                             navigatorApi : NewNavigatorApi,
                             linkHandlerApi: WykopLinkHandlerApi,
                             commentActionListener : LinkCommentActionListener,
                             commentViewListener: LinkCommentViewListener?) : BaseLinkCommentViewHolder(containerView, userManagerApi, settingsPreferencesApi, navigatorApi, linkHandlerApi, commentActionListener, commentViewListener), LayoutContainer {
    override lateinit var embedView: WykopEmbedView

    // Bind correct views
    override var commentContent: TextView = commentContentTextView
    override var replyButton: TextView = replyTextView
    override var collapseButton: ImageView = collapseButtonImageView
    override var authorBadgeStrip: View = authorBadgeStripView
    override var plusButton: PlusVoteButton = plusVoteButton
    override var minusButton: MinusVoteButton = minusVoteButton
    override var moreOptionsButton: TextView = moreOptionsTextView
    override var shareButton: TextView = shareTextView

    companion object {
        const val TYPE_TOP_EMBED = 20
        const val TYPE_TOP_NORMAL = 21
        const val TYPE_TOP_BLOCKED = 22

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(parent: ViewGroup, viewType: Int, userManagerApi: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi, navigatorApi: NewNavigatorApi, linkHandlerApi : WykopLinkHandlerApi, commentActionListener: LinkCommentActionListener, commentViewListener: LinkCommentViewListener?): TopLinkCommentViewHolder {
            val view = TopLinkCommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.top_link_comment_layout, parent, false),
                    userManagerApi,
                    settingsPreferencesApi,
                    navigatorApi,
                    linkHandlerApi,
                    commentActionListener,
                    commentViewListener)

            view.type = viewType

            when (viewType) {
                TYPE_TOP_EMBED -> view.inflateEmbed()
            }
            return view
        }
    }

    override fun bindView(linkComment: LinkComment, isAuthorComment: Boolean, commentId: Int) {
        super.bindView(linkComment, isAuthorComment, commentId)
        authorHeaderView.setAuthorData(linkComment.author, linkComment.date, linkComment.app)
        if (linkComment.isCollapsed) {
            messageTextView.isVisible = true
            messageTextView.text = "${linkComment.childCommentCount} ukrytych komentarzy"
            messageTextView.setOnClickListener {
                commentViewListener?.setCollapsed(linkComment, false)
            }
        } else {
            messageTextView.isVisible = false
        }
    }

    fun inflateEmbed() {
        embedView = wykopEmbedView.inflate() as WykopEmbedView
    }
}
