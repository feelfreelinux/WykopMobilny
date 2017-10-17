package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Comment
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.comment_layout.view.*
import javax.inject.Inject

class CommentView : CardView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var linkHandler : WykopLinkHandlerApi

    init {
        WykopApp.uiInjector.inject(this)
        View.inflate(context, R.layout.comment_layout, this)
        val dpAsPixels = (8 * resources.displayMetrics.density + 0.5f).toInt()
        setContentPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)

        isClickable = true
        isFocusable = true
        setBackgroundResource(R.drawable.comment_background_state_list)
    }

    fun setCommentData(comment: Comment) {
        setupHeader(comment)
        setupFooter(comment)
        setupBody(comment)
    }

    private fun setupHeader(comment : Comment) {
        authorHeaderView.setAuthorData(comment.author, comment.date)
    }

    private fun setupFooter(comment : Comment) {
        voteButton.apply {
            setCommentData(comment)
            isButtonSelected = comment.isVoted
            voteCount = comment.voteCount
        }
    }

    private fun setupBody(comment : Comment) {
        entryContentTextView.prepareBody(comment.body, linkHandler)
        entryImageView.setEmbed(comment.embed)
    }
}