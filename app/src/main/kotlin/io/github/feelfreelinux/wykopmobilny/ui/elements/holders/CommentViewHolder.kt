package io.github.feelfreelinux.wykopmobilny.ui.elements.holders

import android.support.v7.widget.RecyclerView
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
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandler
import kotlinx.android.synthetic.main.comment_layout.view.*
import kotlinx.android.synthetic.main.entry_header.view.*


class CommentViewHolder(val view: View, val callbacks : WykopActionHandler) : RecyclerView.ViewHolder(view) {
    fun bindView(comment : Comment) {
        bindHeader(comment)
        bindBody(comment)
        bindFooter(comment)
    }

    private fun bindHeader(comment : Comment) {
        comment.author.apply {
            val header = view.entryHeader
            header.userNameTextView.apply {
                text = nick
                setTextColor(getGroupColor(group))
            }

            header.avatarImageView.loadImage(avatarUrl)
            val date = comment.date.toPrettyDate()
            header.entryDateTextView.text = date

            app?.let {
                header.entryDateTextView.text = view.context.getString(R.string.date_with_user_app, date, app)
            }

            view.genderStripImageView.setBackgroundResource(getGenderStripResource(sex))
        }
    }

    private fun bindFooter(comment : Comment) {
        view.voteCountTextView.apply {
            setCommentData(comment.entryId, comment.id, comment.voteCount)
            isButtonSelected = comment.isVoted
            voteCount = comment.voteCount
        }
    }

    private fun bindBody(comment : Comment) {
        view.entryContentTextView.prepareBody(comment.body, callbacks)

        view.entryImageView.apply {
            isVisible = false
            comment.embed?.apply {
                isVisible = true
                loadImage(preview)
                if (type == "image") setPhotoViewUrl(url)
            }
        }
    }

}