package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_comment_layout.view.*
import kotlinx.android.synthetic.main.link_comment_menu_bottomsheet.view.*
import kotlin.math.absoluteValue

class LinkCommentWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs), URLClickedListener, LinkCommentView {
    lateinit var comment : LinkComment
    lateinit var presenter : LinkCommentPresenter
    lateinit var settingsApi : SettingsPreferencesApi
    lateinit var userManagerApi : UserManagerApi
    init {
        View.inflate(context, R.layout.link_comment_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }


    fun setLinkCommentData(linkComment: LinkComment, linkPresenter: LinkCommentPresenter, userManager: UserManagerApi, settingsPreferencesApi: SettingsPreferencesApi) {
        comment = linkComment
        userManagerApi = userManager
        presenter = linkPresenter
        presenter.subscribe(this)
        presenter.linkId = linkComment.id
        settingsApi = settingsPreferencesApi
        setupHeader()
        setupBody()
        setupButtons()
    }

    override fun markCommentAsRemoved() {
        commentContentTextView.setText(R.string.commentRemoved)
        commentImageView.isVisible = false
    }

    private fun setupHeader() {
        authorHeaderView.setAuthorData(comment.author, comment.date, comment.app)
    }

    private fun setupBody() {
        commentImageView.setEmbed(comment.embed, settingsApi, presenter.newNavigatorApi)
        comment.body?.let {
            commentContentTextView.prepareBody(comment.body!!, this)
        }
        commentContentTextView.isVisible = !comment.body.isNullOrEmpty()
        val margin = if (comment.id != comment.parentId) 8f else 0f
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, resources.displayMetrics)
        val params = layoutParams as MarginLayoutParams
        params.setMargins(px.toInt(), params.topMargin, params.rightMargin, params.bottomMargin)
        requestLayout()

    }

    fun setStyleForComment(isAuthorComment: Boolean, commentId : Int = -1) {
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

    override fun handleUrl(url: String) {
        presenter.handleUrl(url)
    }

    private fun setupButtons() {
        plusButton.setup(userManagerApi)
        plusButton.text = comment.voteCountPlus.toString()
        minusButton.setup(userManagerApi)
        minusButton.text = comment.voteCountMinus.absoluteValue.toString()
        moreOptionsTextView.setOnClickListener { openLinkCommentMenu() }
        shareTextView.setOnClickListener { shareUrl() }

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
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.link_comment_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            comment_menu_copy.setOnClickListener {
                dialog.dismiss()
            }

            comment_menu_delete.setOnClickListener {
                presenter.deleteComment()
                dialog.dismiss()
            }

            comment_menu_report.setOnClickListener {
                dialog.dismiss()
            }

            comment_menu_edit.setOnClickListener {
                presenter.openEditCommentActivity(comment.body!!)
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
}