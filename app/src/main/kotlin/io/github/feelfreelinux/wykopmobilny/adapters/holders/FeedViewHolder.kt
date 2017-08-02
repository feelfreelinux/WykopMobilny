package io.github.feelfreelinux.wykopmobilny.adapters.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.adapters.CommentClickListener
import io.github.feelfreelinux.wykopmobilny.adapters.TagClickListener
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.VoteResponse
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.feed_layout.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class FeedViewHolder(
        view: View,
        val wam: WykopApiManager
) : RecyclerView.ViewHolder(view) {
    val context = view.context!!
    val prettyTime = PrettyTime(Locale("pl"))

    var commentClickListener: CommentClickListener? = null
    var tagClickListener: TagClickListener? = null

    fun bindItem(entry: Entry) {
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
            else ->
                itemView.entryImageView.gone()
        }
    }

    private fun bindFooter(entry: Entry) {
        itemView.voteCountTextView.run {
            text = context.getString(R.string.votes_count, entry.votes_count)
            isSelected = entry.voted
            disableFor(1000)
            setOnClickListener {
                val voteAction: (Any) -> Unit = {
                    voteResponse ->
                    run {
                        isSelected = !isSelected
                        entry.voted = isSelected
                        entry.votes_count = (voteResponse as VoteResponse).vote
                        text = context.getString(R.string.votes_count, entry.votes_count)
                    }
                }

                if (!isSelected)
                    wam.voteEntry(entry.id, null, voteAction)
                else wam.unvoteEntry(entry.id, null, voteAction)
            }
        }
        itemView.commentsCountTextView.run {
            text = entry.comments_count.toString()
            setOnClickListener {
                commentClickListener?.invoke(entry.id)
            }
        }
    }
}