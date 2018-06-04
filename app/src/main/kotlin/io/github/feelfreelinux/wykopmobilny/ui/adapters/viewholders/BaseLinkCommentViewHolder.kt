package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.View
import android.graphics.Color
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentViewListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.MinusVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.PlusVoteButton
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.link_comment_menu_bottomsheet.view.*
import kotlin.math.absoluteValue


abstract class BaseLinkCommentViewHolder(override val containerView: View,
                                         internal val userManagerApi: UserManagerApi,
                                         internal val settingsPreferencesApi: SettingsPreferencesApi,
                                         internal val navigatorApi: NewNavigatorApi,
                                         internal val linkHandlerApi: WykopLinkHandlerApi,
                                         internal val commentActionListener: LinkCommentActionListener,
                                         internal val commentViewListener: LinkCommentViewListener?) : RecyclableViewHolder(containerView), LayoutContainer {
    val collapseDrawable by lazy {
        val typedArray = containerView.context.obtainStyledAttributes(arrayOf(
                R.attr.collapseDrawable).toIntArray())
        val drawable = typedArray.getDrawable(0)
        typedArray.recycle()
        drawable
    }

    val expandDrawable by lazy {
        val typedArray = containerView.context.obtainStyledAttributes(arrayOf(
                R.attr.expandDrawable).toIntArray())
        val drawable = typedArray.getDrawable(0)
        typedArray.recycle()
        drawable
    }

    companion object {
        fun getViewTypeForComment(comment: LinkComment, forceTop : Boolean = false): Int {
            return if (comment.parentId != comment.id && !forceTop) {
                when {
                    comment.isBlocked -> LinkCommentViewHolder.TYPE_BLOCKED
                    comment.embed == null -> LinkCommentViewHolder.TYPE_NORMAL
                    else -> LinkCommentViewHolder.TYPE_EMBED

                }
            } else {
                when {
                    comment.isBlocked -> TopLinkCommentViewHolder.TYPE_TOP_BLOCKED
                    comment.embed == null -> TopLinkCommentViewHolder.TYPE_TOP_NORMAL
                    else -> TopLinkCommentViewHolder.TYPE_TOP_EMBED
                }
            }
        }
    }

    override fun cleanRecycled() {
    }

    var type: Int = 0
    abstract var embedView: WykopEmbedView

    abstract var commentContent: TextView
    abstract var replyButton: TextView
    abstract var collapseButton: ImageView
    abstract var authorBadgeStrip: View
    abstract var plusButton: PlusVoteButton
    abstract var minusButton: MinusVoteButton
    abstract var moreOptionsButton: TextView
    abstract var shareButton: TextView
    open var collapsedCommentsTextView : TextView? = null

    open fun bindView(linkComment: LinkComment, isAuthorComment: Boolean, commentId: Int = -1) {
        setupBody(linkComment)
        setupButtons(linkComment)
        setStyleForComment(linkComment, isAuthorComment, commentId)
    }


    private fun setupBody(comment: LinkComment) {
        replyButton.isVisible = userManagerApi.isUserAuthorized() && commentViewListener != null
        if (type == LinkCommentViewHolder.TYPE_EMBED || type == TopLinkCommentViewHolder.TYPE_TOP_EMBED) {
            embedView.setEmbed(comment.embed, settingsPreferencesApi, navigatorApi, comment.isNsfw)
        }

        comment.body?.let {
            commentContent.prepareBody(comment.body!!, { linkHandlerApi.handleUrl(it) }, { handleClick(comment) }, settingsPreferencesApi.openSpoilersDialog)
        }

        containerView.setOnClickListener { handleClick(comment) }

        commentContent.isVisible = !comment.body.isNullOrEmpty()
        collapseButton.isVisible = !((comment.id != comment.parentId) || comment.childCommentCount == 0) && commentViewListener != null
    }

    fun handleClick(comment : LinkComment) {
        // Register click listener for comments list
        if (commentViewListener == null) {
            navigatorApi.openLinkDetailsActivity(comment.linkId, comment.id)
        }
    }

    fun setStyleForComment(comment: LinkComment, isAuthorComment: Boolean, commentId: Int = -1) {
        val credentials = userManagerApi.getUserCredentials()
        if (credentials != null && credentials.login == comment.author.nick) {
            authorBadgeStrip.isVisible = true
            authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(containerView.context, R.color.colorBadgeOwn))
        } else if (isAuthorComment) {
            authorBadgeStrip.isVisible = true
            authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(containerView.context, R.color.colorBadgeAuthors))
        } else {
            authorBadgeStrip.isVisible = false
        }

        if (commentId == comment.id) {
            authorBadgeStrip.isVisible = true
            authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(containerView.context, R.color.plusPressedColor))
        }

    }

    private fun setupButtons(comment: LinkComment) {
        plusButton.setup(userManagerApi)
        plusButton.text = comment.voteCountPlus.toString()
        minusButton.setup(userManagerApi)
        minusButton.text = comment.voteCountMinus.absoluteValue.toString()
        moreOptionsButton.setOnClickListener { openLinkCommentMenu(comment) }
        shareButton.setOnClickListener {
            navigatorApi.shareUrl(comment.url)
        }
        plusButton.isEnabled = true
        minusButton.isEnabled = true

        if (comment.isCollapsed) {
            collapseButton.setImageDrawable(expandDrawable)
            collapseButton.setOnClickListener {
                commentViewListener?.setCollapsed(comment, false)
                collapsedCommentsTextView?.isVisible = true
                collapsedCommentsTextView?.text = "${comment.childCommentCount} ukrytych komentarzy"
            }
        } else {
            collapseButton.setImageDrawable(collapseDrawable)
            collapseButton.setOnClickListener {
                collapsedCommentsTextView?.isVisible = false
                commentViewListener?.setCollapsed(comment, true)
            }
        }

        plusButton.voteListener = {
            commentActionListener.digComment(comment)
            minusButton.isEnabled = false
        }

        minusButton.voteListener = {
            commentActionListener.buryComment(comment)
            plusButton.isEnabled = false
        }

        plusButton.unvoteListener = {
            minusButton.isEnabled = false
            commentActionListener.removeVote(comment)
        }

        minusButton.unvoteListener = {
            plusButton.isEnabled = false
            commentActionListener.removeVote(comment)
        }

        when (comment.userVote) {
            1 -> {
                plusButton.isButtonSelected = true
                minusButton.isButtonSelected = false
            }

            0 -> {
                plusButton.isButtonSelected = false
                minusButton.isButtonSelected = false
            }

            -1 -> {
                plusButton.isButtonSelected = false
                minusButton.isButtonSelected = true
            }
        }

        replyButton.setOnClickListener {
            commentViewListener?.replyComment(comment)
        }
    }

    fun openLinkCommentMenu(comment: LinkComment) {
        val activityContext = containerView.getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.link_comment_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)
        (bottomSheetView.parent as View).setBackgroundColor(Color.TRANSPARENT)

        bottomSheetView.apply {
            author.text = comment.author.nick
            date.text = comment.date
            comment.app?.let {
                date.text = context.getString(R.string.date_with_user_app, comment.date, comment.app)
            }

            comment_menu_copy.setOnClickListener {
                dialog.dismiss()
            }

            comment_menu_delete.setOnClickListener {
                ConfirmationDialog(getActivityContext()!!) {
                    commentActionListener.deleteComment(comment)
                }.show()
                dialog.dismiss()
            }

            comment_menu_report.setOnClickListener {
                navigatorApi.openReportScreen(comment.violationUrl)
                dialog.dismiss()
            }

            comment_menu_edit.setOnClickListener {
                navigatorApi.openEditLinkCommentActivity(comment.id, comment.body!!, comment.linkId)
                dialog.dismiss()
            }

            comment_menu_report.isVisible = userManagerApi.isUserAuthorized()

            val canUserEdit = userManagerApi.isUserAuthorized() && comment.author.nick == userManagerApi.getUserCredentials()!!.login
            comment_menu_delete.isVisible = canUserEdit
            comment_menu_edit.isVisible = canUserEdit
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }
}
