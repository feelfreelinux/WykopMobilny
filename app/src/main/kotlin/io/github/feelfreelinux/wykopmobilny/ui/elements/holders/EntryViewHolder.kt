package io.github.feelfreelinux.wykopmobilny.ui.elements.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.EntryResponse
import io.github.feelfreelinux.wykopmobilny.utils.api.getGenderStripResource
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.setPhotoViewUrl
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandler
import kotlinx.android.synthetic.main.entry_header.view.*
import kotlinx.android.synthetic.main.feed_layout.view.*


class EntryViewHolder(val view: View, val callbacks : WykopActionHandler) : RecyclerView.ViewHolder(view) {



    fun bindView(entry : Entry) {
        bindHeader(entry)
        bindBody(entry)
        bindFooter(entry)
    }

    private fun bindHeader(entry : Entry) {
        val header = view.entryHeader
        entry.author.apply {
            header.userNameTextView.apply {
                text = nick
                setTextColor(getGroupColor(group))
            }

            header.avatarImageView.loadImage(entry.author.avatarUrl)
            val date = entry.date.toPrettyDate()
            header.entryDateTextView.text = date

            app?.let {
                header.entryDateTextView.text = view.context.getString(R.string.date_with_user_app, date, app)
            }

            view.genderStripImageView.setBackgroundResource(getGenderStripResource(sex))
        }
    }

    private fun bindFooter(entry : Entry) {
        view.commentsCountTextView.apply {
            text = entry.commentsCount.toString()

            setOnClickListener {
                callbacks.onCommentsClicked(entry.id)
            }
        }

        view.voteCountTextView.apply {
            setEntryData(entry.id, entry.voteCount)
            isButtonSelected = entry.isVoted
            voteCount = entry.voteCount
        }

    }

    private fun bindBody(entry : Entry) {
        view.entryContentTextView.prepareBody(entry.body, callbacks)

        view.entryImageView.apply {
            isVisible = false
            entry.embed?.apply {
                isVisible = true
                loadImage(preview)
                if (type == "image") setPhotoViewUrl(url)
            }
        }
    }

}