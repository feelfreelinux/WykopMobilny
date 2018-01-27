package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.appendNewSpan
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.dialog_voters.view.*
import kotlinx.android.synthetic.main.entry_comment_layout.view.*
import kotlinx.android.synthetic.main.entry_comment_menu_bottomsheet.view.*

class CommentWidget : CardView, CommentView, URLClickedListener {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    lateinit var comment : EntryComment

    var addReceiverListener: ((Author) -> Unit)? = null
    lateinit var presenter : CommentPresenter
    private lateinit var userManagerApi: UserManagerApi
    private lateinit var votersDialogListener : (List<Voter>) -> Unit

    init {
        View.inflate(context, R.layout.entry_comment_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    fun setCommentData(entryComment: EntryComment, userManager : UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi, commentPresenter: CommentPresenter) {
        this.comment = entryComment
        presenter = commentPresenter
        userManagerApi = userManager
        commentPresenter.commentId = entryComment.id
        commentPresenter.subscribe(this)
        voteButton.setup(userManager)
        entryImageView.setEmbed(comment.embed, settingsPreferencesApi, commentPresenter.navigatorApi, entryComment.isNsfw)
        setupHeader()
        setupFooter()
        setupBody()
    }

    fun setStyleForComment(isAuthorComment: Boolean, commentId : Int = -1){
        val credentials = userManagerApi.getUserCredentials()
        if (credentials != null && credentials.login == comment.author.nick) {
            authorBadgeStrip.isVisible = true
            authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBadgeOwn))
        } else if (isAuthorComment) {
            authorBadgeStrip.isVisible = true
            authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBadgeAuthors))
        } else {
            authorBadgeStrip.isVisible = false
        }

        if (commentId == comment.id) {
            authorBadgeStrip.isVisible = true
            authorBadgeStrip.setBackgroundColor(ContextCompat.getColor(context, R.color.plusPressedColor))
        }

    }

    private fun setupHeader() {
        authorHeaderView.setAuthorData(comment.author, comment.date, comment.app)
    }

    private fun setupFooter() {
        voteButton.apply {
            isButtonSelected = comment.isVoted
            voteCount = comment.voteCount
            voteListener = { presenter.voteComment() }
            unvoteListener = { presenter.unvoteComment() }
        }
        shareTextView.setOnClickListener { shareUrl() }
    }

    private fun setupBody() {
        moreOptionsTextView.setOnClickListener { openOptionsMenu() }
        replyTextView.setOnClickListener { addReceiver() }
        if (comment.body.isNotEmpty()) {
            entryContentTextView.isVisible = true
            entryContentTextView.prepareBody(comment.body, this)
        } else entryContentTextView.isVisible = false
    }

    override fun handleUrl(url: String) {
        presenter.handleLink(url)
    }

    fun addReceiver() {
        addReceiverListener?.invoke(comment.author)
    }


    override fun markCommentAsRemoved() {
        entryContentTextView.setText(R.string.commentRemoved)
        entryImageView.isVisible = false
    }

    override fun markCommentUnvoted(voteCount: Int) {
        voteButton.apply {
            isButtonSelected = false
            isEnabled = true
        }.voteCount = voteCount
        comment.isVoted = false
        comment.voteCount = voteCount
    }

    override fun markCommentVoted(voteCount: Int) {
        voteButton.apply {
            isButtonSelected = true
            isEnabled = true
        }.voteCount = voteCount
        comment.isVoted = true
        comment.voteCount = voteCount
    }

    override fun showErrorDialog(e: Throwable) = context.showExceptionDialog(e)

    fun openOptionsMenu() {
        val activityContext = getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.entry_comment_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            entry_comment_menu_copy.setOnClickListener {
                presenter.copyContent(comment)
                dialog.dismiss()
            }

            entry_comment_menu_edit.setOnClickListener {
                presenter.editEntryComment(comment)
                dialog.dismiss()
            }

            entry_comment_menu_delete.setOnClickListener {
                presenter.deleteComment()
                dialog.dismiss()
            }

            entry_comment_menu_voters.setOnClickListener {
                openVotersMenu()
                dialog.dismiss()
            }

            entry_comment_menu_report.setOnClickListener {
                presenter.reportContent()
                dialog.dismiss()
            }

            entry_comment_menu_report.isVisible = userManagerApi.isUserAuthorized()

            val canUserEdit = userManagerApi.isUserAuthorized() && comment.author.nick == userManagerApi.getUserCredentials()!!.login
            entry_comment_menu_delete.isVisible = canUserEdit
            entry_comment_menu_edit.isVisible = canUserEdit
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    fun openVotersMenu() {
        val activityContext = getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val votersDialogView = activityContext.layoutInflater.inflate(R.layout.dialog_voters, null)
        votersDialogView.votersTextView.isVisible = false
        dialog.setContentView(votersDialogView)
        votersDialogListener = {
            if (dialog.isShowing) {
                votersDialogView.progressView.isVisible = false
                votersDialogView.votersTextView.isVisible = true
                val spannableStringBuilder = SpannableStringBuilder()
                it
                        .map { it.author }
                        .forEachIndexed {
                            index, author ->
                            val span = ForegroundColorSpan(getGroupColor(author.group))
                            spannableStringBuilder.appendNewSpan(author.nick, span, 0)
                            if (index < it.size - 1) spannableStringBuilder.append(", ")
                        }
                if (spannableStringBuilder.isEmpty()) {
                    votersDialogView.votersTextView.gravity = Gravity.CENTER
                    spannableStringBuilder.append(context.getString(R.string.dialogNoVotes))
                }
                votersDialogView.votersTextView.text = spannableStringBuilder
            }
        }
        dialog.show()
        presenter.getVoters()
    }

    override fun showVoters(voters : List<Voter>) {
        votersDialogListener(voters)
    }

    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(getActivityContext()!!)
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }

    val url : String
        get() = "https://www.wykop.pl/wpis/${comment.entryId}/#comment-${comment.id}"
}