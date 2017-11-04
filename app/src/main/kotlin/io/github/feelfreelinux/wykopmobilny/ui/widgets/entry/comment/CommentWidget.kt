package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Comment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.CommentMenuDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.CommentMenuDialogInterface
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.comment_layout.view.*
import javax.inject.Inject

class CommentWidget : CardView, CommentMenuDialogInterface, CommentView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    lateinit var comment: Comment

    var addReceiverListener: ((Author) -> Unit)? = null

    @Inject lateinit var linkHandler: WykopLinkHandlerApi
    @Inject lateinit var userManagerApi: UserManagerApi
    @Inject lateinit var clipboardHelper: ClipboardHelperApi
    @Inject lateinit var presenter: CommentPresenter
    @Inject lateinit var navigator: NavigatorApi

    init {
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        View.inflate(context, R.layout.comment_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    fun setCommentData(comment: Comment) {
        this.comment = comment
        setupHeader()
        setupFooter()
        setupBody()

        setOnLongClickListener {
            CommentMenuDialog(context, comment.author, userManagerApi, this).show()
            true
        }
    }

    private fun setupHeader() {
        authorHeaderView.setAuthorData(comment.author, comment.date)
    }

    private fun setupFooter() {
        voteButton.apply {
            setCommentData(comment)
            isButtonSelected = comment.isVoted
            voteCount = comment.voteCount
        }
    }

    private fun setupBody() {
        entryContentTextView.prepareBody(comment.body, { linkHandler.handleUrl(getActivityContext()!!, it) })
        entryImageView.setEmbed(comment.embed)
    }

    override fun addReceiver() {
        addReceiverListener?.invoke(comment.author)
    }

    override fun copyContent() {
        clipboardHelper.copyTextToClipboard(comment.body.removeHtml())
    }

    override fun editComment() {
        navigator.openEditEntryCommentActivity(getActivityContext()!!, comment.body.removeHtml(), comment.entryId, comment.id)
    }

    override fun removeComment() {
        presenter.deleteComment(comment.entryId, comment.id)
    }

    override fun markCommentAsRemoved() {
        entryContentTextView.setText(R.string.commentRemoved)
        entryImageView.isVisible = false
    }

    override fun showErrorDialog(e: Throwable) = context.showExceptionDialog(e)
}