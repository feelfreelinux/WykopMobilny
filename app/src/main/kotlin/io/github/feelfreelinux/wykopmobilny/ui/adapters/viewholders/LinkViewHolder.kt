package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import kotlinx.android.synthetic.main.link_layout.view.*
import javax.inject.Inject


class LinkViewHolder(val view: View) : RecyclerView.ViewHolder(view), URLClickedListener {
    fun bindView(link : Link) {
        view.apply {
            title.text = link.title.removeHtml()
            image.isVisible = link.preview != null
            link.preview?.let { image.loadImage(link.preview.stripImageCompression()) }
            description.text = link.description.removeHtml()
            diggCountTextView.text = link.voteCount.toString()
            commentsCountTextView.text = link.commentsCount.toString()
            dateTextView.text = link.date.toPrettyDate()
            hotBadgeStrip.isVisible = link.isHot
            setOnClickListener {
                view.getActivityContext()!!.startActivity(LinkDetailsActivity.createIntent(view.getActivityContext()!!, link))
            }
        }
    }

    override fun handleUrl(url: String) {
        // @TODO something here
    }
}