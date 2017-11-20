package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.comment_layout.view.*
import javax.inject.Inject

class CommentWidget : CardView, CommentView, URLClickedListener {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    lateinit var comment : EntryComment

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

    fun setCommentData(entryComment: EntryComment) {
        this.comment = entryComment
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
            setCommentData(comment)
            isButtonSelected = comment.isVoted
            voteCount = comment.voteCount
        }
    }

    private fun setupBody() {
        moreOptionsTextView.setOnClickListener { openOptionsMenu() }
        replyTextView.setOnClickListener { addReceiver() }
        entryContentTextView.prepareBody(comment.body, this)
        entryImageView.setEmbed(comment.embed)
    }

    override fun handleUrl(url: String) {
        linkHandler.handleUrl(getActivityContext()!!, url)
    }

    fun addReceiver() {
        addReceiverListener?.invoke(comment.author)
    }

    fun copyContent() {
        clipboardHelper.copyTextToClipboard(comment.body.removeHtml())
    }

    fun editComment() {
        navigator.openEditEntryCommentActivity(getActivityContext()!!, comment.body, comment.entryId, comment.id)
    }

    fun removeComment() {
        presenter.deleteComment(comment.id)
    }

    override fun markCommentAsRemoved() {
        entryContentTextView.setText(R.string.commentRemoved)
        entryImageView.isVisible = false
    }

    override fun showErrorDialog(e: Throwable) = context.showExceptionDialog(e)

    fun openOptionsMenu() {
        val popUpMenu = PopupMenu(context, moreOptionsTextView)
        if (userManagerApi.isUserAuthorized()) {
            if (comment.author.nick == userManagerApi.getUserCredentials()!!.login) {
                popUpMenu.menuInflater.inflate(R.menu.entry_comment_menu_authors_loggedin, popUpMenu.menu)
                popUpMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.copy_content -> copyContent()
                        R.id.edit_content -> editComment()
                        R.id.delete_content -> removeComment()
                    }
                    true
                }
            } else {
                popUpMenu.menuInflater.inflate(R.menu.entry_menu_loggedin, popUpMenu.menu)
                popUpMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.copy_content -> copyContent()
                    }
                    true
                }
            }
        } else {
            popUpMenu.menuInflater.inflate(R.menu.entry_menu_loggedin, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.copy_content -> copyContent()
                }
                true
            }
        }
        popUpMenu.show()
    }
}