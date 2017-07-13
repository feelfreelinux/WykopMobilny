package io.github.feelfreelinux.wykopmobilny.adapters

import android.graphics.Color
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.github.feelfreelinux.wykopmobilny.*
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class MikroblogListAdapter(val dataSet : ArrayList<Entry>, val loadMoreListener: LoadMoreListener) : RecyclerView.Adapter<MikroblogListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_wpis, parent, false))


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val parsedBody : Spanned
        val entry = dataSet[position]
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
             parsedBody = Html.fromHtml(entry.body, Html.FROM_HTML_MODE_COMPACT)
        else  parsedBody = Html.fromHtml(entry.body)

        // detect load more
        if (position == dataSet.size - 1 && !loadMoreListener.loading) loadMoreListener.loadMore()
        holder?.itemView?.findViewById<TextView>(R.id.body)?.text = parsedBody
        holder?.itemView?.findViewById<Button>(R.id.comment_count)?.text = entry.comments_count.toString()
        val btn = holder?.itemView?.findViewById<Button>(R.id.vote_count)
        btn?.text = "+" + entry.votes_count.toString()
        var btnPressed = false
        btn?.setOnClickListener {
            btnPressed = !btnPressed
            btn?.isPressed = btnPressed
        }


        val image = holder?.itemView?.findViewById<ImageView>(R.id.avatar)
        Picasso.with(image?.context).load(entry.author.avatarUrl).into(image)

        holder?.itemView?.findViewById<TextView>(R.id.login)?.run {
            setTextColor(getGroupColor(entry.author.role))
            text = entry.author.nick
        }

        var genderStrip = holder?.itemView?.findViewById<View>(R.id.genderStrip)
        if (entry.author.gender == "male") {
            genderStrip?.visibility = View.VISIBLE
            genderStrip?.setBackgroundColor(Color.parseColor("#46abf2"))
        }
        else if (entry.author.gender == "female") {
            genderStrip?.visibility = View.VISIBLE
            genderStrip?.setBackgroundColor(Color.parseColor("#f246d0"))
        }
        else genderStrip?.visibility = View.INVISIBLE

        val prettyTime = PrettyTime(Locale("pl"))

        holder?.itemView?.findViewById<TextView>(R.id.date)?.text = prettyTime.format(entry.date)

        val embedView = holder?.itemView?.findViewById<ImageView>(R.id.embed_image)
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