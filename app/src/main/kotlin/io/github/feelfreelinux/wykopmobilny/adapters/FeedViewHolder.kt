package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.feed_layout.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context = view.context!!
    val prettyTime = PrettyTime(Locale("pl"))

    var commentClickListener: CommentClickListener? = null
    var tagClickListener: TagClickListener? = null
    var entryVoteClickListener: VoteClickListener? = null

    fun bindItem(entry: Entry) {
        bindHeader(entry)
        bindContent(entry)
        bindFooter(entry)
    }

    private fun bindHeader(entry: Entry) {
        Picasso.with(context).load(entry.author.avatarUrl).fit().centerCrop().into(itemView.avatarImageView)
        itemView.entryDateTextView.text = prettyTime.format(entry.date)

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

        when (entry.embed.type) {
            "image", "video" -> {
                itemView.entryImageView.visible()
                Picasso.with(context).load(entry.embed.preview).into(itemView.entryImageView)
            }
            else -> {
                itemView.entryImageView.gone()
            }
        }
    }

    private fun bindFooter(entry: Entry) {
        itemView.voteCountTextView.text = context.getString(R.string.votes_count, entry.votes_count)
        itemView.voteCountTextView.isSelected = entry.voted
        itemView.voteCountTextView.setOnClickListener {
            itemView.voteCountTextView.isSelected = !itemView.voteCountTextView.isSelected
            itemView.voteCountTextView.disableFor(1000)
            entryVoteClickListener?.invoke(entry) { isSuccess, voteCount ->
                if (isSuccess){
                    entry.voted = !entry.voted
                    entry.votes_count = voteCount
                    itemView.voteCountTextView.text = context.getString(R.string.votes_count, voteCount)
                }else{
                    itemView.voteCountTextView.isSelected = !itemView.voteCountTextView.isSelected
                }
            }
        }
        itemView.commentsCountTextView.text = entry.comments_count.toString()
        itemView.commentsCountTextView.setOnClickListener {
            commentClickListener?.invoke(entry.id)
        }
    }
}