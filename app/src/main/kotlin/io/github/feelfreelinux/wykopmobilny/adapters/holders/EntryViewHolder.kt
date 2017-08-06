package io.github.feelfreelinux.wykopmobilny.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.callbacks.FeedClickCallbackInterface
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.entry_header.view.*
import kotlinx.android.synthetic.main.feed_layout.view.*


class EntryViewHolder(val view: View, val callbacks : FeedClickCallbackInterface) : RecyclerView.ViewHolder(view) {



    fun bindView(entry : Entry) {
        bindHeader(entry)
        bindBody(entry)
        bindFooter(entry)
    }

    fun bindHeader(entry : Entry) {
        val header = view.entryHeader
        header.userNameTextView.apply {
            text = entry.author
            setTextColor(getGroupColor(entry.authorGroup))
        }

        header.avatarImageView.loadImage(entry.authorAvatarMed)
        val date = entry.date.toPrettyDate()
        header.entryDateTextView.text = date

        entry.app?.let {
            header.entryDateTextView.text = view.context.getString(R.string.date_with_user_app, date, entry.app)
        }

        view.genderStripImageView.setBackgroundResource(getGenderStripResource(entry.authorSex))
    }

    fun bindFooter(entry : Entry) {
        view.commentsCountTextView.apply {
            text = entry.commentCount.toString()

            setOnClickListener {
                callbacks.onCommentsClicked(entry.id)
            }
        }

        view.voteCountTextView.apply {
            isSelected = entry.userVote > 0
            text = context.getString(R.string.votes_count, entry.voteCount)
            setOnClickListener {
                callbacks.onVoteClicked(entry.id, null, isSelected, {
                    text = context.getString(R.string.votes_count, it.vote)
                    isSelected = !isSelected
                    entry.userVote = if (isSelected) 1 else 0
                })
            }
        }
    }

    fun bindBody(entry : Entry) {
        view.entryContentTextView.prepareBody(entry.body, {
            callbacks.onTagClicked(it)
        })

        view.entryImageView.apply {
            gone()
            entry.embed?.let {
                visible()
                loadImage(entry.embed!!.preview)
                if (entry.embed!!.type == "image") setPhotoViewUrl(entry.embed!!.url)
            }
        }
    }

}