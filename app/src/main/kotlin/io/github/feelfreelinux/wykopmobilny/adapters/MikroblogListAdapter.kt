package io.github.feelfreelinux.wykopmobilny.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.card_wpis.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

typealias VoteClickListener = (Entry, Boolean) -> Unit
typealias TagClickListener = (String) -> Unit
typealias CommentClickListener = (Int) -> Unit

class MikroblogListAdapter(
        private val commentVoteClickListener: VoteClickListener,
        private val entryVoteClickListener: VoteClickListener,
        private val tagClickListener: TagClickListener,
        private val commentClickListener: CommentClickListener) : RecyclerView.Adapter<MikroblogListAdapter.ViewHolder>() {

    var isPager: Boolean = true
    var dataSet = emptyList<Entry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_wpis, parent, false)
        return ViewHolder(
                view,
                commentVoteClickListener,
                entryVoteClickListener,
                tagClickListener,
                commentClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(dataSet[position], isPager)
    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(itemView: View,
                     val commentVoteClickListener: VoteClickListener,
                     val entryVoteClickListener: VoteClickListener,
                     val tagClickListener: TagClickListener,
                     val commentClickListener: CommentClickListener) : RecyclerView.ViewHolder(itemView) {

        val context = itemView.context!!
        val picasso = Picasso.Builder(context).build()!!
        val prettyTime = PrettyTime(Locale("pl"))

        fun bindItem(entry: Entry, isPager: Boolean) {
            val commentButton = itemView.comment_count
            bindVoteButton(isPager, entry, commentButton)
            itemView.body.setTagsClickable(entry.body, tagClickListener)
            bindCommentView(entry, commentButton)

            picasso.load(entry.author.avatarUrl).into(itemView.avatar)

            itemView.login.run {
                setTextColor(getGroupColor(entry.author.role))
                text = entry.author.nick
            }

            bindGenderStrip(entry)


            itemView.date.text = prettyTime.format(entry.date)

            val embedView = itemView.embed_image
            when (entry.embed.type) {
                "image", "video" -> {
                    embedView?.visibility = View.VISIBLE
                    picasso.load(entry.embed.preview).into(embedView)
                }
                else -> {
                    embedView.gone()
                }
            }
        }

        private fun bindVoteButton(isPager: Boolean, entry: Entry, commentButton: TextView) {
            val votes: TextView
            if (isPager) {
                // Disable top mirko_control_button button, enable bottom button
                itemView.vote_count.visibility = View.GONE
                votes = itemView.vote_count_bottom
                votes.visibility = View.VISIBLE
                votes.text = "+" + entry.votes_count.toString()
                // comment button click action
                commentButton.isClickable = true
                commentButton.setOnClickListener {
                    commentClickListener.invoke(entry.id)

                }
            } else {
                commentButton.isClickable = false
                // Disable bottom mirko_control_button button, enable top button
                itemView.vote_count_bottom.visibility = View.GONE
                votes = itemView.vote_count
                votes.visibility = View.VISIBLE
                votes.text = "+" + entry.votes_count.toString()

                val commentBtnLayout = commentButton.layoutParams as RelativeLayout.LayoutParams
                commentBtnLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                commentButton.layoutParams = commentBtnLayout
            }
            val drawable = if (entry.voted) R.drawable.mirko_control_button_clicked else R.drawable.mirko_control_button

            votes.setBackgroundResource(drawable)

            votes.setOnClickListener {
                var vote = true
                if (drawable == R.drawable.mirko_control_button_clicked) vote = false

                if (entry.isComment && entry.entryId != null) {
                    commentVoteClickListener.invoke(entry, vote)
                } else {
                    entryVoteClickListener.invoke(entry, vote)
                }
                votes.setBackgroundResource(drawable)
            }
        }

        private fun bindCommentView(entry: Entry, commentButton: TextView) {
            if (entry.isComment)
                commentButton.visibility = View.GONE
            else {
                commentButton.text = entry.comments_count.toString()
                commentButton.visibility = View.VISIBLE
            }
        }

        private fun bindGenderStrip(entry: Entry) {
            val genderStrip = itemView.genderStrip
            with(genderStrip){
                when (entry.author.gender) {
                    "male" -> {
                        visible()
                        setBackgroundColor(Color.parseColor("#46abf2"))
                    }
                    "female" -> {
                        visible()
                        setBackgroundColor(Color.parseColor("#f246d0"))
                    }
                    else -> {
                        invisible()
                    }
                }
            }

        }
    }
}