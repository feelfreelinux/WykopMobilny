package io.github.feelfreelinux.wykopmobilny.ui.elements.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.Entry
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

    private fun bindFooter(entry : Entry) {
        view.commentsCountTextView.apply {
            text = entry.commentCount.toString()

            setOnClickListener {
                callbacks.onCommentsClicked(entry.id)
            }
        }

        view.voteCountTextView.apply {
            setEntryData(entry.id, entry.voteCount)
            isButtonSelected = entry.userVote > 0
            voteCount = entry.voteCount
        }

    }

    private fun bindBody(entry : Entry) {
        view.entryContentTextView.prepareBody(entry.body, callbacks)

        view.entryImageView.apply {
            isVisible = false
            entry.embed?.let {
                isVisible = true
                loadImage(entry.embed!!.preview)
                if (entry.embed!!.type == "image") setPhotoViewUrl(entry.embed!!.url)
            }
        }
    }

}