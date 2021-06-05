package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.databinding.CommentListItemBinding
import io.github.feelfreelinux.wykopmobilny.databinding.EntryCommentMenuBottomsheetBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.drawBadge
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.confirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentViewListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.copyText
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.layoutInflater
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.stripWykopFormatting
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.extensions.LayoutContainer

class EntryCommentViewHolder(
    private val binding: CommentListItemBinding,
    private val userManagerApi: UserManagerApi,
    private val settingsPreferencesApi: SettingsPreferencesApi,
    private val navigatorApi: NewNavigatorApi,
    private val linkHandlerApi: WykopLinkHandlerApi,
    private val commentActionListener: EntryCommentActionListener,
    private val commentViewListener: EntryCommentViewListener?,
    private val enableClickListener: Boolean
) : RecyclableViewHolder(binding.root), LayoutContainer {

    companion object {
        const val TYPE_EMBED = 9
        const val TYPE_NORMAL = 10
        const val TYPE_BLOCKED = 11

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
            commentActionListener: EntryCommentActionListener,
            commentViewListener: EntryCommentViewListener?,
            enableClickListener: Boolean
        ): EntryCommentViewHolder {
            val view = EntryCommentViewHolder(
                CommentListItemBinding.inflate(parent.layoutInflater, parent, false),
                userManagerApi,
                settingsPreferencesApi,
                navigatorApi,
                linkHandlerApi,
                commentActionListener,
                commentViewListener,
                enableClickListener,
            )

            view.type = viewType

            if (viewType == TYPE_EMBED) view.inflateEmbed()

            return view
        }

        fun getViewTypeForEntryComment(comment: EntryComment): Int {
            return when {
                comment.isBlocked -> TYPE_BLOCKED
                comment.embed == null -> TYPE_NORMAL
                else -> TYPE_EMBED
            }
        }
    }

    override val containerView = binding.root

    var type: Int = TYPE_NORMAL
    private var isAuthorComment: Boolean = false
    private var isOwnEntry: Boolean = false
    lateinit var embedView: WykopEmbedView

    private val isEmbedViewResized: Boolean
        get() = ::embedView.isInitialized && embedView.resized

    fun bindView(comment: EntryComment, entryAuthor: Author? = null, highlightCommentId: Int = 0) {
        setupHeader(comment)
        setupButtons(comment)
        setupBody(comment)
        isOwnEntry = entryAuthor?.nick == userManagerApi.getUserCredentials()?.login
        isAuthorComment = entryAuthor?.nick == comment.author.nick
        setStyleForComment(comment, highlightCommentId)
    }

    private fun setupHeader(comment: EntryComment) {
        comment.author.apply {
            binding.avatarView.setAuthor(this)
            binding.avatarView.setOnClickListener { navigatorApi.openProfileActivity(nick) }
            binding.authorTextView.apply {
                text = nick
                setTextColor(context.getGroupColor(group))
                setOnClickListener { navigatorApi.openProfileActivity(nick) }
            }
            binding.patronBadgeTextView.isVisible = badge != null
            badge?.let {
                try {
                    badge?.drawBadge(binding.patronBadgeTextView)
                } catch (exception: Throwable) {
                    Log.w(this::class.simpleName, "Couldn't draw badge", exception)
                }
            }
            binding.dateTextView.text = comment.date.replace(" temu", "")
            comment.app?.let {
                binding.dateTextView.text =
                    containerView.context.getString(R.string.date_with_user_app, comment.date.replace(" temu", ""), comment.app)
            }
        }
    }

    private fun setupButtons(comment: EntryComment) {
        binding.moreOptionsTextView.setOnClickListener {
            openOptionsMenu(comment)
        }

        // Only show reply view in entry details
        binding.replyTextView.isVisible = userManagerApi.isUserAuthorized() && commentViewListener != null
        binding.replyTextView.setOnClickListener { commentViewListener?.addReply(comment.author) }

        // Setup vote button
        with(binding.voteButton) {
            isEnabled = true
            isButtonSelected = comment.isVoted
            voteCount = comment.voteCount
            voteListener = {
                commentActionListener.voteComment(comment)
            }
            unvoteListener = {
                commentActionListener.unvoteComment(comment)
            }
            setup(userManagerApi)
        }

        // Setup share button
        binding.shareTextView.setOnClickListener {
            navigatorApi.shareUrl(comment.url)
        }
    }

    private fun setupBody(comment: EntryComment) {
        // Add URL and click handler if body is not empty
        binding.replyTextView.isVisible = userManagerApi.isUserAuthorized()
        binding.replyTextView.setOnClickListener { commentViewListener?.addReply(comment.author) }
        binding.quoteTextView.isVisible = userManagerApi.isUserAuthorized()
        binding.quoteTextView.setOnClickListener { commentViewListener?.quoteComment(comment) }
        if (comment.body.isNotEmpty()) {
            binding.entryContentTextView.isVisible = true
            binding.entryContentTextView.prepareBody(
                comment.body,
                { linkHandlerApi.handleUrl(it) },
                { handleClick(comment) },
                settingsPreferencesApi.openSpoilersDialog
            )
        } else binding.entryContentTextView.isVisible = false

        if (comment.embed != null && type == TYPE_EMBED) {
            embedView.setEmbed(comment.embed, settingsPreferencesApi, navigatorApi, comment.isNsfw)
        }

        if (enableClickListener) {
            containerView.setOnClickListener {
                handleClick(comment)
            }
        }
    }

    private fun openOptionsMenu(comment: EntryComment) {
        val activityContext = containerView.getActivityContext()!!
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(activityContext)
        val bottomSheetView = EntryCommentMenuBottomsheetBinding.inflate(activityContext.layoutInflater)
        dialog.setContentView(bottomSheetView.root)
        (bottomSheetView.root.parent as View).setBackgroundColor(Color.TRANSPARENT)
        bottomSheetView.apply {
            author.text = comment.author.nick
            date.text = comment.fullDate
            comment.app?.let {
                date.text = root.context.getString(R.string.date_with_user_app, comment.fullDate, comment.app)
            }

            entryCommentMenuCopy.setOnClickListener {
                it.context.copyText(comment.body.stripWykopFormatting(), "entry-comment-body")
                dialog.dismiss()
            }

            entryCommentMenuEdit.setOnClickListener {
                navigatorApi.openEditEntryCommentActivity(comment.body, comment.entryId, comment.id)
                dialog.dismiss()
            }

            entryCommentMenuDelete.setOnClickListener {
                confirmationDialog(it.context) {
                    commentActionListener.deleteComment(comment)
                }.show()
                dialog.dismiss()
            }

            entryCommentMenuVoters.setOnClickListener {
                commentActionListener.getVoters(comment)
                dialog.dismiss()
            }

            entryCommentMenuReport.setOnClickListener {
                navigatorApi.openReportScreen(comment.violationUrl)
                dialog.dismiss()
            }

            entryCommentMenuReport.isVisible = userManagerApi.isUserAuthorized()

            val canUserEdit = userManagerApi.isUserAuthorized() &&
                comment.author.nick == userManagerApi.getUserCredentials()?.login
            entryCommentMenuDelete.isVisible = canUserEdit || isOwnEntry
            entryCommentMenuEdit.isVisible = canUserEdit
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.root.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.root.height
        }
        dialog.show()
    }

    private fun handleClick(comment: EntryComment) {
        if (enableClickListener) {
            navigatorApi.openEntryDetailsActivity(comment.entryId, isEmbedViewResized)
        }
    }

    fun inflateEmbed() {
        embedView = binding.entryImageViewStub.inflate() as WykopEmbedView
    }

    private fun setStyleForComment(comment: EntryComment, commentId: Int = -1) {
        val credentials = userManagerApi.getUserCredentials()
        if (credentials != null && credentials.login == comment.author.nick) {
            binding.authorBadgeStrip.isVisible = true
            binding.authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(containerView.context, R.color.colorBadgeOwn))
        } else if (isAuthorComment) {
            binding.authorBadgeStrip.isVisible = true
            binding.authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(containerView.context, R.color.colorBadgeAuthors))
        } else {
            binding.authorBadgeStrip.isVisible = false
        }

        if (commentId == comment.id) {
            binding.authorBadgeStrip.isVisible = true
            binding.authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(containerView.context, R.color.plusPressedColor))
        }
    }
}
