package io.github.wykopmobilny.ui.adapters.viewholders

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.LinkCommentLayoutBinding
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.models.dataclass.drawBadge
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentViewListener
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.wykopmobilny.ui.widgets.buttons.MinusVoteButton
import io.github.wykopmobilny.ui.widgets.buttons.PlusVoteButton
import io.github.wykopmobilny.utils.api.getGroupColor
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

class LinkCommentViewHolder(
    private val binding: LinkCommentLayoutBinding,
    userManagerApi: UserManagerApi,
    settingsPreferencesApi: SettingsPreferencesApi,
    navigatorApi: NewNavigatorApi,
    linkHandlerApi: WykopLinkHandlerApi,
    commentActionListener: LinkCommentActionListener,
    commentViewListener: LinkCommentViewListener
) : BaseLinkCommentViewHolder(
    binding.root,
    userManagerApi,
    settingsPreferencesApi,
    navigatorApi,
    linkHandlerApi,
    commentViewListener,
    commentActionListener
) {

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
                LinkCommentLayoutBinding.inflate(parent.layoutInflater, parent, false),
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
    override var commentContent: TextView = binding.commentContentTextView
    override var replyButton: TextView = binding.replyTextView
    override var collapseButton: ImageView = binding.collapseButtonImageView
    override var authorBadgeStrip: View = binding.authorBadgeStripView
    override var plusButton: PlusVoteButton = binding.plusVoteButton
    override var minusButton: MinusVoteButton = binding.minusVoteButton
    override var moreOptionsButton: TextView = binding.moreOptionsTextView
    override var shareButton: TextView = binding.shareTextView

    override fun bindView(linkComment: LinkComment, isAuthorComment: Boolean, commentId: Int) {
        super.bindView(linkComment, isAuthorComment, commentId)

        // setup header
        linkComment.author.apply {
            binding.avatarView.setAuthor(this)
            binding.avatarView.setOnClickListener { navigatorApi.openProfileActivity(nick) }
            binding.authorTextView.apply {
                text = nick
                setTextColor(context.getGroupColor(group))
                setOnClickListener { }
            }
            binding.patronBadgeTextView.isVisible = badge != null
            badge?.let {
                try {
                    badge?.drawBadge(binding.patronBadgeTextView)
                } catch (exception: Throwable) {
                    Log.w(this::class.simpleName, "Couldn't draw badge", exception)
                }
            }
            binding.dateTextView.text = linkComment.date.replace(" temu", "")
            linkComment.app?.let {
                binding.dateTextView.text =
                    itemView.context.getString(R.string.date_with_user_app, linkComment.date.replace(" temu", ""), linkComment.app)
            }
        }
    }

    fun inflateEmbed() {
        embedView = binding.wykopEmbedView.inflate() as WykopEmbedView
    }
}
