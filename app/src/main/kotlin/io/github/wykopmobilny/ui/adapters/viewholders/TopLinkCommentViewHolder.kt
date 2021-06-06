package io.github.wykopmobilny.ui.adapters.viewholders

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import io.github.wykopmobilny.databinding.TopLinkCommentLayoutBinding
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentViewListener
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.wykopmobilny.ui.widgets.buttons.MinusVoteButton
import io.github.wykopmobilny.ui.widgets.buttons.PlusVoteButton
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

class TopLinkCommentViewHolder(
    private val binding: TopLinkCommentLayoutBinding,
    userManagerApi: UserManagerApi,
    settingsPreferencesApi: SettingsPreferencesApi,
    navigatorApi: NewNavigatorApi,
    linkHandlerApi: WykopLinkHandlerApi,
    commentActionListener: LinkCommentActionListener,
    commentViewListener: LinkCommentViewListener?
) : BaseLinkCommentViewHolder(
    binding.root,
    userManagerApi,
    settingsPreferencesApi,
    navigatorApi,
    linkHandlerApi,
    commentViewListener,
    commentActionListener
) {

    override lateinit var embedView: WykopEmbedView

    // Bind correct views
    override var commentContent: TextView = binding.commentContentTextView
    override var replyButton: TextView = binding.replyTextView
    override var collapseButton: ImageView = binding.collapseButtonImageView
    override var authorBadgeStrip: View = binding.authorBadgeStripView
    override var plusButton: PlusVoteButton = binding.plusVoteButton
    override var minusButton: MinusVoteButton = binding.minusVoteButton
    override var moreOptionsButton: TextView = binding.moreOptionsTextView
    override var shareButton: TextView = binding.shareTextView

    companion object {
        const val TYPE_TOP_EMBED = 20
        const val TYPE_TOP_NORMAL = 21
        const val TYPE_TOP_BLOCKED = 22

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
            commentViewListener: LinkCommentViewListener?
        ): TopLinkCommentViewHolder {
            val view = TopLinkCommentViewHolder(
                TopLinkCommentLayoutBinding.inflate(parent.layoutInflater, parent, false),
                userManagerApi,
                settingsPreferencesApi,
                navigatorApi,
                linkHandlerApi,
                commentActionListener,
                commentViewListener
            )

            view.type = viewType

            when (viewType) {
                TYPE_TOP_EMBED -> view.inflateEmbed()
            }
            return view
        }
    }

    override fun bindView(linkComment: LinkComment, isAuthorComment: Boolean, commentId: Int) {
        super.bindView(linkComment, isAuthorComment, commentId)

        binding.authorHeaderView.setAuthorData(linkComment.author, linkComment.date, linkComment.app)
        if (linkComment.isCollapsed) {
            binding.messageTextView.isVisible = linkComment.childCommentCount > 0
            binding.messageTextView.text = "${linkComment.childCommentCount} ukrytych komentarzy"
            binding.messageTextView.setOnClickListener { commentViewListener?.setCollapsed(linkComment, false) }
        } else {
            binding.messageTextView.isVisible = false
        }
    }

    fun inflateEmbed() {
        embedView = binding.wykopEmbedView.inflate() as WykopEmbedView
    }
}
