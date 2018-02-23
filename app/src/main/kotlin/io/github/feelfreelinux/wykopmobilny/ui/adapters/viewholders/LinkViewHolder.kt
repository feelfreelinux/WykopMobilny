package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.link_layout.*

class LinkViewHolder(override val containerView: View, settingsApi: SettingsPreferencesApi) : BaseLinkViewHolder(containerView, settingsApi) {
    override fun bindView(link: Link) {
        this.link = link
        if (settingsApi.linkImagePosition == "top" || settingsApi.linkImagePosition == "bottom") {
            title.maxLines = 5
            description.maxLines = 10
        }
        getCorrectImageView().isVisible = link.preview != null
        if (settingsApi.linkShowImage) {
            getCorrectImageView().apply {
                isVisible = link.preview != null
                link.preview?.let { loadImage(link.preview) }
            }
        }
        if (settingsApi.linkShowAuthor) {
            authorHeaderView.setAuthorData(link.author!!, link.date, link.app)
            authorHeaderView.isVisible = true
            lineTop.isVisible = true
            dateTextView.isVisible = false
        }

        title.text = link.title
        description.text = link.description

        diggCountTextView.voteCount = link.voteCount
        diggCountTextView.isEnabled = false
        diggCountTextView.setVoteState(link.userVote)

        commentsCountTextView.text = link.commentsCount.toString()
        dateTextView.text = link.date
        hotBadgeStrip.isVisible = link.isHot
        commentsCountTextView.setOnClickListener {
            openLinkDetail()
        }
        shareTextView.setOnClickListener {
            shareUrl()
        }
        containerView.setOnClickListener {
            openLinkDetail()
        }
    }

    override fun setWidgetAlpha(alpha: Float) {
        getCorrectImageView().alpha = alpha
        title.alpha = alpha
        description.alpha = alpha
    }

    fun getCorrectImageView(): ImageView {
        return when (settingsApi.linkImagePosition) {
            "top" -> image_top
            "right" -> image_right
            "bottom" -> image_bottom
            else -> image_left
        }
    }

    override fun cleanRecycled() {
        containerView.apply {
            title.text = null
            description.text = null
            diggCountTextView.text = null
            commentsCountTextView.text = null
            setOnClickListener(null)
            GlideApp.with(this).clear(image_left)
            GlideApp.with(this).clear(image_right)
            GlideApp.with(this).clear(image_top)
            GlideApp.with(this).clear(image_bottom)
        }
    }
}