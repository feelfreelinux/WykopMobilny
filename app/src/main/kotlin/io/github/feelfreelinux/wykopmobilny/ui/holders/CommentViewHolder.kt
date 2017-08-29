package io.github.feelfreelinux.wykopmobilny.ui.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.api.getGenderStripResource
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.FeedClickCallbackInterface
import io.github.feelfreelinux.wykopmobilny.api.Comment
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import kotlinx.android.synthetic.main.entry_header.view.*
import kotlinx.android.synthetic.main.feed_layout.view.*


class CommentViewHolder(val view: View, val callbacks : FeedClickCallbackInterface) : RecyclerView.ViewHolder(view) {
    fun bindView(comment : Comment) {
        bindHeader(comment)
        bindBody(comment)
        bindFooter(comment)
    }

    private fun bindHeader(comment : Comment) {
        val header = view.entryHeader
        header.userNameTextView.apply {
            text = comment.author
            setTextColor(getGroupColor(comment.authorGroup))
        }

        header.avatarImageView.loadImage(comment.authorAvatarMed)
        val date = comment.date.toPrettyDate()
        header.entryDateTextView.text = date

        comment.app?.let {
            header.entryDateTextView.text = view.context.getString(R.string.date_with_user_app, date, comment.app)
        }

        view.genderStripImageView.setBackgroundResource(getGenderStripResource(comment.authorSex))
    }

    private fun bindFooter(comment : Comment) {
        view.voteCountTextView.apply {
            isSelected = comment.userVote > 0
            text = context.getString(R.string.votes_count, comment.voteCount)
            setOnClickListener {
                callbacks.onVoteClicked(comment.entryId, comment.id, isSelected, {
                    text = context.getString(R.string.votes_count, it.vote)
                    isSelected = !isSelected
                    comment.userVote = if (isSelected) 1 else 0
                })
            }
        }
    }

    private fun bindBody(comment : Comment) {
        view.entryContentTextView.prepareBody(comment.body, callbacks)

        view.entryImageView.apply {
            isVisible = false
            comment.embed?.let {
                isVisible = true
                loadImage(comment.embed!!.preview)
                if (comment.embed!!.type == "image") setPhotoViewUrl(comment.embed!!.url)
            }
        }
    }

}