package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import android.content.Context
import android.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.ConfirmationDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.profile.ProfileActivity
import io.github.feelfreelinux.wykopmobilny.ui.widgets.WykopEmbedView
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.MinusVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.PlusVoteButton
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_comment_menu_bottomsheet.view.*
import kotlin.math.absoluteValue

abstract class BaseLinkCommentWidget(context: Context, attrs: AttributeSet) : androidx.constraintlayout.widget.ConstraintLayout(context, attrs), LinkCommentView {
    lateinit var comment : LinkComment
    lateinit var presenter : LinkCommentPresenter
    lateinit var settingsApi : SettingsPreferencesApi
    lateinit var userManagerApi : UserManagerApi
    var shouldEnableClickListener = false
    lateinit var collapseListener : (Boolean, Int) -> Unit
    lateinit var commentContent: TextView
    lateinit var replyButton: TextView
    lateinit var collapseButton : ImageView
    lateinit var authorBadgeStrip : View
    lateinit var plusButton : PlusVoteButton
    lateinit var minusButton : MinusVoteButton
    lateinit var moreOptionsButton: TextView
    lateinit var shareButton: TextView


    lateinit var commentImageView : WykopEmbedView
    init {
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.itemBackgroundColorStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    abstract fun bindViews()

    var showHiddenCommentsCountCard : (Boolean) -> Unit = {}


    open fun setLinkCommentData(linkComment: LinkComment, linkPresenter: LinkCommentPresenter, userManager: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi) {
        bindViews()
        comment = linkComment
        userManagerApi = userManager
        presenter = linkPresenter
        presenter.subscribe(this)
        presenter.linkId = linkComment.id
        settingsApi = settingsPreferencesApi
        setupBody()
        setupButtons()
    }

    override fun markCommentAsRemoved() {
        commentContent.setText(R.string.commentRemoved)
        commentImageView.isVisible = false
    }


    fun openProfile(username : String) {
        getActivityContext()!!.startActivity(ProfileActivity.createIntent(getActivityContext()!!, username))
    }

    private fun setupBody() {
        replyButton.isVisible = userManagerApi.isUserAuthorized()
        commentImageView.setEmbed(comment.embed, settingsApi, presenter.newNavigatorApi, comment.isNsfw)
        val clickListener =  { presenter.handleUrl(url) }
        if (shouldEnableClickListener) setOnClickListener { clickListener() }
        else setOnClickListener(null)
        comment.body?.let {
            commentContent.prepareBody(comment.body!!, { presenter.handleUrl(it) }, if (shouldEnableClickListener) clickListener else null, settingsApi.openSpoilersDialog)
        }
        collapseButton.setOnClickListener {
            commentContent.isVisible = false
        }
        commentContent.isVisible = !comment.body.isNullOrEmpty()
        if ((comment.id != comment.parentId) || comment.childCommentCount == 0) {
            showHiddenCommentsCountCard(false)
            collapseButton.isVisible = false
        } else collapseButton.isVisible = true
    }

    private fun setupButtons() {
        plusButton.setup(userManagerApi)
        plusButton.text = comment.voteCountPlus.toString()
        minusButton.setup(userManagerApi)
        minusButton.text = comment.voteCountMinus.absoluteValue.toString()
        moreOptionsButton.setOnClickListener { openLinkCommentMenu() }
        shareButton.setOnClickListener { shareUrl() }
        if (comment.isCollapsed) collapseComment(false)
        else expandComment(false)

        plusButton.voteListener = {
            presenter.voteUp()
            minusButton.isEnabled = false
        }

        minusButton.voteListener = {
            presenter.voteDown()
            plusButton.isEnabled = false
        }

        plusButton.unvoteListener = {
            minusButton.isEnabled = false
            presenter.voteCancel()
        }

        minusButton.unvoteListener = {
            plusButton.isEnabled = false
            presenter.voteCancel()
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
    }

    override fun setVoteCount(voteResponse: LinkVoteResponse) {
        comment.voteCount = voteResponse.voteCount
        comment.voteCountMinus = voteResponse.voteCountMinus
        comment.voteCountPlus = voteResponse.voteCountPlus
        plusButton.voteCount = voteResponse.voteCountPlus
        minusButton.voteCount = voteResponse.voteCountMinus.absoluteValue
    }

    override fun markVotedMinus() {
        comment.userVote = -1
        minusButton.isEnabled = true
        plusButton.isEnabled = true
        minusButton.isButtonSelected = true
        plusButton.isButtonSelected = false
    }

    override fun markVotedPlus() {
        comment.userVote = 1
        minusButton.isEnabled = true
        plusButton.isEnabled = true
        minusButton.isButtonSelected = false
        plusButton.isButtonSelected = true
    }

    override fun markVoteRemoved() {
        comment.userVote = 0
        minusButton.isEnabled = true
        plusButton.isEnabled = true
        minusButton.isButtonSelected = false
        plusButton.isButtonSelected = false
    }

    fun openLinkCommentMenu() {
        val activityContext = getActivityContext()!!
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(activityContext)
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
                    presenter.deleteComment()
                }.show()
                dialog.dismiss()
            }

            comment_menu_report.setOnClickListener {
                presenter.reportContent(comment.violationUrl)
                dialog.dismiss()
            }

            comment_menu_edit.setOnClickListener {
                presenter.openEditCommentActivity(comment.id, comment.body!!)
                dialog.dismiss()
            }

            comment_menu_report.isVisible = userManagerApi.isUserAuthorized()

            val canUserEdit = userManagerApi.isUserAuthorized() && comment.author.nick == userManagerApi.getUserCredentials()!!.login
            comment_menu_delete.isVisible = canUserEdit
            comment_menu_edit.isVisible = canUserEdit
        }

        val mBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    fun collapseComment(shouldCallListener : Boolean = true) {
        val typedArray = context.obtainStyledAttributes(arrayOf(
                R.attr.expandDrawable).toIntArray())
        collapseButton.setImageDrawable(typedArray.getDrawable(0))
        typedArray.recycle()
        collapseButton.setOnClickListener {
            expandComment()
        }
        showHiddenCommentsCountCard(true)
        if (shouldCallListener) collapseListener(true, comment.id)
    }

    fun expandComment(shouldCallListener : Boolean = true) {
        val typedArray = context.obtainStyledAttributes(arrayOf(
                R.attr.collapseDrawable).toIntArray())
        collapseButton.setImageDrawable(typedArray.getDrawable(0))
        typedArray.recycle()
        collapseButton.setOnClickListener {
            collapseComment()
        }
        showHiddenCommentsCountCard(false)
        if (shouldCallListener) collapseListener(false, comment.id)
    }

    override fun showErrorDialog(e: Throwable) {
        context.showExceptionDialog(e)
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
        get() = "https://www.wykop.pl/link/${comment.linkId}/#comment-${comment.id}"

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (::presenter.isInitialized)
            presenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (::presenter.isInitialized) presenter.unsubscribe()
    }
}