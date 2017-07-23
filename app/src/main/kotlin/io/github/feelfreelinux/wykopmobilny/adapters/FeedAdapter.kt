package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.gone
import io.github.feelfreelinux.wykopmobilny.utils.setTagsClickable
import io.github.feelfreelinux.wykopmobilny.utils.visible
import kotlinx.android.synthetic.main.feed_layout.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class FeedAdapter(
        private val commentVoteClickListener: VoteClickListener,
        private val entryVoteClickListener: VoteClickListener,
        private val tagClickListener: TagClickListener,
        private val commentClickListener: CommentClickListener
) : RecyclerView.Adapter<FeedViewHolder>() {

    var dataSet = emptyList<Entry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bindItem(dataSet[position])
        holder.commentClickListener = commentClickListener
        holder.tagClickListener = tagClickListener
        holder.entryVoteClickListener = entryVoteClickListener
    }

    override fun getItemCount() = dataSet.size
}

class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context = view.context!!
    val prettyTime = PrettyTime(Locale("pl"))

    var commentClickListener: CommentClickListener? = null
    var tagClickListener: TagClickListener? = null
    var entryVoteClickListener: VoteClickListener? = null

    fun bindItem(entry: Entry) {
        itemView.userNameTextView.text = entry.author.nick
        itemView.userNameTextView.setTextColor(getGroupColor(entry.author.role))
        itemView.entryContentTextView.setTagsClickable(entry.body, tagClickListener)
        itemView.commentsCountTextView.text = entry.comments_count.toString()
        itemView.voteCountTextView.text = context.getString(R.string.votes_count, entry.comments_count)
        itemView.entryDateTextView.text = prettyTime.format(entry.date)

        itemView.voteCountTextView.setOnClickListener {
            entryVoteClickListener?.invoke(entry, entry.voted)
        }

        itemView.commentsCountTextView.setOnClickListener {
            commentClickListener?.invoke(entry.id)
        }

        when (entry.author.gender) {
            "male" -> itemView.genderStripImageView.setBackgroundResource(R.drawable.male_strip)
            "female" -> itemView.genderStripImageView.setBackgroundResource(R.drawable.female_strip)
            else -> itemView.genderStripImageView.setBackgroundResource(0)
        }

        Picasso.with(context).load(entry.author.avatarUrl).fit().centerCrop().into(itemView.avatarImageView)

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

}
