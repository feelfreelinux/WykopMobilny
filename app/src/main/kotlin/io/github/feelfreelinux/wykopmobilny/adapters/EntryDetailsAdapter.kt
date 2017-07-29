package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.item_entry_layout.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class EntryDetailsAdapter : RecyclerView.Adapter<EntryDetailsViewHolder>() {

    var entryData = emptyList<Entry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: EntryDetailsViewHolder, position: Int) {
        holder.bindView(entryData[position])
    }

    override fun getItemCount() = entryData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_entry_layout, parent, false)
        return EntryDetailsViewHolder(view)
    }
}

class EntryDetailsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context = view.context!!
    val prettyTime = PrettyTime(Locale("pl"))

    fun bindView(entry: Entry) {
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
        itemView.entryContentTextView.prepareBody(entry.body, {})
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
//            entryVoteClickListener?.invoke(entry) { isSuccess, voteCount ->
//                if (isSuccess) {
//                    entry.voted = !entry.voted
//                    entry.votes_count = voteCount
//                    itemView.voteCountTextView.text = context.getString(R.string.votes_count, voteCount)
//                } else {
//                    itemView.voteCountTextView.isSelected = !itemView.voteCountTextView.isSelected
//                }
//            }
        }
    }

}


