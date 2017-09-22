package io.github.feelfreelinux.wykopmobilny.ui.elements.comment_view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Comment
import io.github.feelfreelinux.wykopmobilny.utils.api.getGenderStripResource
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.setPhotoViewUrl
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandlerImpl
import kotlinx.android.synthetic.main.comment_layout.view.*
import kotlinx.android.synthetic.main.entry_header.view.*

class CommentView : CardView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val callbacks = WykopActionHandlerImpl(context)

    init {
        View.inflate(context, R.layout.comment_layout, this)
        val dpAsPixels = (8 * resources.displayMetrics.density + 0.5f).toInt()
        setContentPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
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
        voteCountTextView.apply {
            setCommentData(comment.entryId, comment.id, comment.voteCount)
            isButtonSelected = comment.isVoted
            voteCount = comment.voteCount
        }
    }

    private fun setupBody(comment : Comment) {
        entryContentTextView.prepareBody(comment.body, callbacks)

        entryImageView.apply {
            isVisible = false
            comment.embed?.apply {
                isVisible = true
                loadImage(preview)
                if (type == "image") setPhotoViewUrl(url)
            }
        }
    }
}