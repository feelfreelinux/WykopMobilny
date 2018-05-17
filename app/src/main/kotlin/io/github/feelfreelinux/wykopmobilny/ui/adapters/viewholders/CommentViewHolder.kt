package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.Color
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentViewListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.survey.SurveyWidget
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.copyText
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.badge_list_item.*
import kotlinx.android.synthetic.main.comment_list_item.*
import kotlinx.android.synthetic.main.entry_menu_bottomsheet.view.*


typealias EntryCommentListener = (EntryComment) -> Unit

class EntryCommentViewHolder(override val containerView: View,
                             private val userManagerApi: UserManagerApi,
                             private val settingsPreferencesApi: SettingsPreferencesApi,
                             private val navigatorApi : NewNavigatorApi,
                             private val linkHandlerApi: WykopLinkHandlerApi,
                             private val commentActionListener : EntryCommentActionListener,
                             private val commentViewListener: EntryCommentViewListener) : RecyclableViewHolder(containerView), LayoutContainer {
    var type : Int = TYPE_NORMAL
    lateinit var embedView : WykopEmbedView

    val isEmbedViewResized : Boolean
        get() = ::embedView.isInitialized && embedView.resized

    companion object {
        const val TYPE_EMBED = 9
        const val TYPE_NORMAL = 10
        const val TYPE_BLOCKED = 11

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(parent: ViewGroup, viewType: Int, userManagerApi: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi, navigatorApi: NewNavigatorApi, linkHandlerApi : WykopLinkHandlerApi, commentActionListener: EntryCommentActionListener, commentViewListener: EntryCommentViewListener): EntryCommentViewHolder {
            val view = EntryCommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_list_item, parent, false),
                    userManagerApi,
                    settingsPreferencesApi,
                    navigatorApi,
                    linkHandlerApi,
                    commentActionListener,
                    commentViewListener)

            view.type = viewType

            when (viewType) {
                TYPE_EMBED -> view.inflateEmbed()
            }
            return view
        }
    }

    fun bindView(comment: EntryComment) {
        setupHeader(comment)
        setupButtons(comment)
        setupBody(comment)
    }

    private fun setupHeader(comment : EntryComment) {
        comment.author.apply {
            avatarView.setAuthor(this)
            avatarView.setOnClickListener { navigatorApi.openProfileActivity(nick) }
            authorTextView.apply {
                text = nick
                setTextColor(context.getGroupColor(group))
                setOnClickListener { navigatorApi.openProfileActivity(nick) }
            }
            dateTextView.text = comment.date.replace(" temu", "")
            comment.app?.let {
                dateTextView.text = containerView.context.getString(R.string.date_with_user_app, comment.date.replace(" temu", ""), comment.app)
            }
        }
    }

    fun setupButtons(comment : EntryComment) {
        moreOptionsTextView.setOnClickListener {
            openOptionsMenu(comment)
        }

        // Only show reply view in entry details
        replyTextView.isVisible = userManagerApi.isUserAuthorized()
        replyTextView.setOnClickListener({ commentViewListener.addReply(comment.author) })


        // Setup vote button
        with(voteButton) {
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
        shareTextView.setOnClickListener {
            //navigatorApi.shareUrl(comment.url)
        }

    }

    private fun setupBody(comment : EntryComment) {
        // Add URL and click handler if body is not empty
        replyTextView.isVisible = userManagerApi.isUserAuthorized()
        replyTextView.setOnClickListener { commentViewListener.addReply(comment.author) }
        quoteTextView.isVisible = userManagerApi.isUserAuthorized()
        quoteTextView.setOnClickListener { commentViewListener.quoteComment(comment) }
        if (comment.body.isNotEmpty()) {
            entryContentTextView.isVisible = true
            entryContentTextView.prepareBody(comment.body, { linkHandlerApi.handleUrl(it) }, null, settingsPreferencesApi.openSpoilersDialog)
        } else entryContentTextView.isVisible = false


        if ( type == TYPE_EMBED) {
            embedView.setEmbed(comment.embed!!, settingsPreferencesApi, navigatorApi, comment.isNsfw)
        }

    }

    fun openOptionsMenu(comment : EntryComment) {
        val activityContext = containerView.getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.entry_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)
        (bottomSheetView.parent as View).setBackgroundColor(Color.TRANSPARENT)
        bottomSheetView.apply {
            author.text = comment.author.nick
            date.text = comment.date
            comment.app?.let {
                date.text = context.getString(R.string.date_with_user_app, comment.date, comment.app)
            }

            entry_menu_copy.setOnClickListener {
                context.copyText(comment.body, "entry-body")
                dialog.dismiss()
            }

            entry_menu_copy_entry_url.setOnClickListener {
                context.copyText(comment.url, "entry-url")
                dialog.dismiss()
            }

            entry_menu_edit.setOnClickListener {
                navigatorApi.openEditEntryActivity(comment.body, comment.id)
                dialog.dismiss()
            }

            entry_menu_delete.setOnClickListener {
                ConfirmationDialog(getActivityContext()!!) {
                    commentActionListener.deleteComment(comment)
                }.show()
                dialog.dismiss()
            }

            entry_menu_report.setOnClickListener {
                navigatorApi.openReportScreen(comment.violationUrl)
                dialog.dismiss()
            }

            entry_menu_voters.setOnClickListener {
                //entryActionListener.getVoters(entry)
                dialog.dismiss()
            }

            entry_menu_report.isVisible = userManagerApi.isUserAuthorized()

            val canUserEdit = userManagerApi.isUserAuthorized() && comment.author.nick == userManagerApi.getUserCredentials()!!.login
            entry_menu_delete.isVisible = canUserEdit
            entry_menu_edit.isVisible = canUserEdit
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    override fun cleanRecycled() {
    }

    fun inflateEmbed() {
        embedView = entryImageViewStub.inflate() as WykopEmbedView
    }

    fun setStyleForComment(isAuthorComment: Boolean, comment : EntryComment, commentId : Int = -1){
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

}