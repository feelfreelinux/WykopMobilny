package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import kotlinx.android.synthetic.main.link_layout.view.*


class LinkViewHolder(val view: View) : RecyclerView.ViewHolder(view)  {
    val linksPreferences by lazy { LinksPreferences(view.context) }
    fun bindView(link : Link) {
        view.apply {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(R.attr.linkViewClicked, typedValue, true)
            title.text = link.title.removeHtml()
            image.isVisible = link.preview != null
            link.preview?.let { image.loadImage(link.preview.stripImageCompression()) }
            description.text = link.description.removeHtml()
            diggCountTextView.voteCount = link.voteCount
            diggCountTextView.isEnabled = false
            diggCountTextView.setVoteState(link.userVote)

            commentsCountTextView.text = link.commentsCount.toString()
            dateTextView.text = link.date.toPrettyDate()
            hotBadgeStrip.isVisible = link.isHot
            val gotPreviouslySelected = linksPreferences.readLinksIds.contains("link_${link.id}")
            if (gotPreviouslySelected) {
                constraintLayout.setBackgroundColor(ContextCompat.getColor(context, typedValue.resourceId))
            } else {
                constraintLayout.setBackgroundColor(Color.TRANSPARENT)
            }
            setOnClickListener {
                if (!gotPreviouslySelected) {
                    constraintLayout.setBackgroundColor(ContextCompat.getColor(context, typedValue.resourceId))
                    linksPreferences.readLinksIds = linksPreferences.readLinksIds.plusElement("link_${link.id}")
                }
                view.getActivityContext()!!.startActivity(LinkDetailsActivity.createIntent(view.getActivityContext()!!, link))
            }
        }
    }

}