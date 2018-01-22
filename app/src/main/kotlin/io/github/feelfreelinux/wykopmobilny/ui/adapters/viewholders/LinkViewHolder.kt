package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.link_layout.view.*


class LinkViewHolder(val view: View) : RecyclerView.ViewHolder(view)  {
    fun bindView(link : Link) {
        view.apply {
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
            setOnClickListener {
                view.getActivityContext()!!.startActivity(LinkDetailsActivity.createIntent(view.getActivityContext()!!, link))
            }
        }
    }

}