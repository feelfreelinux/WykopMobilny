package io.github.feelfreelinux.wykopmobilny.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.activities.MikroblogEntryView
import io.github.feelfreelinux.wykopmobilny.activities.TagViewActivity
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.SpoilerTagHandler
import io.github.feelfreelinux.wykopmobilny.objects.WykopApiData
import io.github.feelfreelinux.wykopmobilny.utils.LinkSpan
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager
import io.github.feelfreelinux.wykopmobilny.utils.getGroupColor
import org.json.JSONObject
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
typealias tagClickListener = (String) -> Unit
typealias entryOpenListener = (Int) -> Unit

class MikroblogListAdapter(val dataSet: ArrayList<Entry>, val isPager: Boolean, val tagClickListener: tagClickListener, val entryOpenListener: entryOpenListener, val wamData : WykopApiData) : RecyclerView.Adapter<MikroblogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_wpis, parent, false))

    fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : LinkSpan() {
            override fun onClick(tv: View) {
                if(span.url.first() == '#')
                    tagClickListener.invoke(span.url.replace("#", ""))

            }
        }

        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    fun setTextViewHTML(text: TextView, html: String) {
        val sequence : Spannable

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            sequence = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT, null, SpoilerTagHandler()) as Spannable
        else  sequence = Html.fromHtml(html, null, SpoilerTagHandler()) as Spannable

        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls)
            makeLinkClickable(strBuilder, span)

        text.text = strBuilder
        text.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val entry = dataSet[position]
        val commentButton = holder?.itemView?.findViewById<TextView>(R.id.comment_count)
        val votes : TextView

        val wam = WykopApiManager(wamData, commentButton?.context!!)
        // detect load more, disable comment button
        if ( isPager ) {
            // Disable top mirko_control_button button, enable bottom button
            holder.itemView?.findViewById<TextView>(R.id.vote_count)?.visibility = View.GONE
            votes = holder.itemView?.findViewById<TextView>(R.id.vote_count_bottom) as TextView
            votes.visibility = View.VISIBLE
            votes.text = "+" + entry.votes_count.toString()
            // comment button click action
            commentButton.isClickable = true
            commentButton.setOnClickListener {
                entryOpenListener(entry.id)
            }
        }
        else {
            commentButton.isClickable = false
            // Disable bottom mirko_control_button button, enable top button
            holder.itemView?.findViewById<TextView>(R.id.vote_count_bottom)?.visibility = View.GONE
            votes = holder.itemView?.findViewById<TextView>(R.id.vote_count) as TextView
            votes.visibility = View.VISIBLE
            votes.text = "+" + entry.votes_count.toString()

            val commentBtnLayout = commentButton.layoutParams as RelativeLayout.LayoutParams
            commentBtnLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            commentButton.layoutParams = commentBtnLayout
        }
        var drawable = R.drawable.mirko_control_button
        if (entry.voted)
            drawable = R.drawable.mirko_control_button_clicked
        votes.setBackgroundResource(drawable)

        votes.setOnClickListener{
            var vote = true
            if (drawable == R.drawable.mirko_control_button_clicked)
                vote = false

            var type = "entry"
            if (entry.isComment && entry.entryId != null) {
                type = "comment"
                wam.voteEntry(type, entry.entryId as Int, entry.id, vote, object : WykopApiManager.WykopApiAction{
                    override fun success(json: JSONObject) {
                        votes.text = "+" + json.getInt("vote")
                        entry.voted = vote
                        if (vote) drawable = R.drawable.mirko_control_button_clicked
                        else drawable = R.drawable.mirko_control_button
                        votes.setBackgroundResource(drawable)
                    }
                })
            } else {
                wam.voteEntry(type, entry.id, null, vote, object : WykopApiManager.WykopApiAction{
                    override fun success(json: JSONObject) {
                        votes.text = "+" + json.getInt("vote")
                        entry.voted = vote
                        if (vote) drawable = R.drawable.mirko_control_button_clicked
                        else drawable = R.drawable.mirko_control_button
                        votes.setBackgroundResource(drawable)
                    }
                })
            }
            votes.setBackgroundResource(drawable)
        }

        setTextViewHTML(holder.itemView?.findViewById<TextView>(R.id.body)!!, entry.body)
        if(entry.isComment)
            commentButton.visibility = View.GONE
        else {
            commentButton.text = entry.comments_count.toString()
            commentButton.visibility = View.VISIBLE
        }


        val image = holder.itemView?.findViewById<ImageView>(R.id.avatar)
        Picasso.with(image?.context).load(entry.author.avatarUrl).into(image)

        holder.itemView?.findViewById<TextView>(R.id.login)?.run {
            setTextColor(getGroupColor(entry.author.role))
            text = entry.author.nick
        }

        val genderStrip = holder.itemView?.findViewById<View>(R.id.genderStrip)
        if (entry.author.gender == "male") {
            genderStrip?.visibility = View.VISIBLE
            genderStrip?.setBackgroundColor(Color.parseColor("#46abf2"))
        } else if (entry.author.gender == "female") {
            genderStrip?.visibility = View.VISIBLE
            genderStrip?.setBackgroundColor(Color.parseColor("#f246d0"))
        }
        else genderStrip?.visibility = View.INVISIBLE

        val prettyTime = PrettyTime(Locale("pl"))

        holder.itemView?.findViewById<TextView>(R.id.date)?.text = prettyTime.format(entry.date)

        val embedView = holder.itemView?.findViewById<ImageView>(R.id.embed_image)


        when(entry.embed.type) {
            "image" -> {
                embedView?.visibility = View.VISIBLE
                Picasso.with(embedView?.context)
                        .load(entry.embed.preview)
                        .into(embedView)
            }
            "video" -> {
                embedView?.visibility = View.VISIBLE
                Picasso.with(embedView?.context).load(entry.embed.preview).into(embedView)
            }
            "null" -> {
                embedView?.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}