package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.View
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.link_layout.*


class LinkViewHolder(override val containerView: View) : RecyclableViewHolder(containerView), LayoutContainer {
    val linksPreferences by lazy { LinksPreferences(containerView.context) }
    fun bindView(link : Link) {
            title.text = link.title
            image.isVisible = link.preview != null
            link.preview?.let { image.loadImage(link.preview) }
            description.text = link.description
            diggCountTextView.voteCount = link.voteCount
            diggCountTextView.isEnabled = false
            diggCountTextView.setVoteState(link.userVote)

            commentsCountTextView.text = link.commentsCount.toString()
            dateTextView.text = link.date
            hotBadgeStrip.isVisible = link.isHot
            if (link.gotSelected) {
                image.alpha = 0.6f
                title.alpha = 0.6f
                description.alpha = 0.6f
            } else {
                image.alpha = 1f
                title.alpha = 1f
                description.alpha = 1f
            }
            containerView.setOnClickListener {
                containerView.getActivityContext()!!.startActivity(LinkDetailsActivity.createIntent(containerView.getActivityContext()!!, link))
                if (!link.gotSelected) {
                    image.alpha = 0.6f
                    title.alpha = 0.6f
                    description.alpha = 0.6f
                    link.gotSelected = true
                    linksPreferences.readLinksIds = linksPreferences.readLinksIds.plusElement("link_${link.id}")
                }
        }
    }

    override fun cleanRecycled() {
            title.text = null
            description.text = null
            diggCountTextView.text = null
            commentsCountTextView.text = null
            containerView.setOnClickListener(null)
            GlideApp.with(containerView).clear(image)

    }
}