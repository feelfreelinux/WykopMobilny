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
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.link_comment_layout.*

class LinkCommentViewHolder(
    override val containerView: View,
    userManagerApi: UserManagerApi,
    settingsPreferencesApi: SettingsPreferencesApi,
    navigatorApi: NewNavigatorApi,
    linkHandlerApi: WykopLinkHandlerApi,
    commentActionListener: LinkCommentActionListener,
    commentViewListener: LinkCommentViewListener
) : BaseLinkCommentViewHolder(
    containerView,
    userManagerApi,
    settingsPreferencesApi,
    navigatorApi,
    linkHandlerApi,
    commentViewListener,
    commentActionListener
), LayoutContainer {

    companion object {
        const val TYPE_EMBED = 17
        const val TYPE_NORMAL = 18
        const val TYPE_BLOCKED = 19

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(
            parent: ViewGroup,
            viewType: Int,
            userManagerApi: UserManagerApi,
            settingsPreferencesApi: SettingsPreferencesApi,
            navigatorApi: NewNavigatorApi,
            linkHandlerApi: WykopLinkHandlerApi,
            commentActionListener: LinkCommentActionListener,
            commentViewListener: LinkCommentViewListener
        ): LinkCommentViewHolder {
            val view = LinkCommentViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.link_comment_layout, parent, false),
                userManagerApi,
                settingsPreferencesApi,
                navigatorApi,
                linkHandlerApi,
                commentActionListener,
                commentViewListener
            )

            view.type = viewType

            when (viewType) {
                TYPE_EMBED -> view.inflateEmbed()
            }
            return view
        }
    }

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

    override fun bindView(linkComment: LinkComment, isAuthorComment: Boolean, commentId: Int) {
        super.bindView(linkComment, isAuthorComment, commentId)

        // setup header
        linkComment.author.apply {
            avatarView.setAuthor(this)
            avatarView.setOnClickListener { navigatorApi.openProfileActivity(nick) }
            authorTextView.apply {
                text = nick
                setTextColor(context.getGroupColor(group))
                setOnClickListener { }
            }
            dateTextView.text = linkComment.date.replace(" temu", "")
            linkComment.app?.let {
                dateTextView.text =
                        containerView.context.getString(R.string.date_with_user_app, linkComment.date.replace(" temu", ""), linkComment.app)
            }
        }
    }

    fun inflateEmbed() {
        embedView = wykopEmbedView.inflate() as WykopEmbedView
    }
}
