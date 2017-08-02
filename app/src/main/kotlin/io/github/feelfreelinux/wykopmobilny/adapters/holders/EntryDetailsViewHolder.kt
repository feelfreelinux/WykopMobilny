package io.github.feelfreelinux.wykopmobilny.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.adapters.TagClickListener
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.item_entry_layout.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class EntryDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context = view.context!!
    val prettyTime = PrettyTime(Locale("pl"))

    var tagClickListener: TagClickListener? = null

    fun bindView(entry: Entry) {
        bindHeader(entry)
        bindContent(entry)
        bindFooter(entry)
    }

    private fun bindHeader(entry: Entry) {
        itemView.avatarImageView.loadImage(entry.author.avatarUrl)
        itemView.entryDateTextView.run {
            text = prettyTime.format(entry.date)
            if (entry.app != null) itemView.entryDateTextView.run {
                text = text.toString() + " via ${entry.app}"
            }
        }
        itemView.userNameTextView.text = entry.author.nick
        itemView.userNameTextView.setTextColor(getGroupColor(entry.author.role))

        when (entry.author.gender) {
            "male" -> itemView.genderStripImageView.setBackgroundResource(R.drawable.male_strip)
            "female" -> itemView.genderStripImageView.setBackgroundResource(R.drawable.female_strip)
            else -> itemView.genderStripImageView.setBackgroundResource(0)
        }
    }

    private fun bindContent(entry: Entry) {
        itemView.entryContentTextView.prepareBody(entry.body, tagClickListener)
        when (entry.embed?.type) {
            "image", "video" -> {
                itemView.entryImageView.run {
                    visible()
                    loadImage(entry.embed.preview)
                    setPhotoViewUrl(entry.embed.url)
                }

            }
            else -> {
                itemView.entryImageView.gone()
            }
        }
    }

    private fun bindFooter(entry: Entry) {
        itemView.voteCountTextView.run {
            text = context.getString(R.string.votes_count, entry.votes_count)
            isSelected = entry.voted
            setOnClickListener {
                isSelected = !itemView.voteCountTextView.isSelected
                disableFor(1000)
            }
        }
    }

}

