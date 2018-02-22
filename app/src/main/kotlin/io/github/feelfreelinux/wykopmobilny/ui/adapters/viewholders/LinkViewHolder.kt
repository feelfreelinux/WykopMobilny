package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.link_layout.*

class LinkViewHolder(override val containerView: View, val settingsApi: SettingsPreferencesApi) : RecyclableViewHolder(containerView), LayoutContainer {
    val linksPreferences by lazy { LinksPreferences(containerView.context) }

    companion object {
        fun createLinkViewHolder(settingsPreferencesApi: SettingsPreferencesApi, parent: ViewGroup): LinkViewHolder {
            val vh = LinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_layout, parent, false), settingsPreferencesApi)
            return vh
        }
    }

    lateinit var link: Link
    val diggCountDrawable by lazy {
        val typedArray = containerView.context.obtainStyledAttributes(arrayOf(
                R.attr.digCountDrawable).toIntArray())
        val selectedDrawable = typedArray.getDrawable(0)
        typedArray.recycle()
        selectedDrawable
    }

    fun bindView(link: Link) {
        this.link = link
        if (settingsApi.linkSimpleList) {
            constraintLayoutSimple.isVisible = true
            simple_digg.setBackground( when (link.userVote) {
                "dig" -> ContextCompat.getDrawable(containerView.context, R.drawable.ic_dig_vote)
                "bury" -> ContextCompat.getDrawable(containerView.context, R.drawable.ic_dig_bury)
                else -> diggCountDrawable
            } )
            simple_digg_count.text = link.voteCount.toString()
            simple_title.text = link.title
            hotBadgeStripSimple.isVisible = link.isHot
            simple_digg_hot.isVisible = link.isHot
            if(settingsApi.linkShowImage) {
                simple_image.isVisible = link.preview != null
                link.preview?.let { simple_image.loadImage(link.preview) }
            }
            if (link.gotSelected) {
                simple_image.alpha = 0.6f
                simple_title.alpha = 0.6f
            } else {
                simple_image.alpha = 1f
                simple_title.alpha = 1f
            }
        } else {
            constraintLayout.isVisible = true
            if(settingsApi.linkShowImage) {
                when (settingsApi.linkImagePosition) {
                    "left" -> {
                        image_left.isVisible = link.preview != null
                        link.preview?.let { image_left.loadImage(link.preview) }
                    }
                    "right" -> {
                        image_right.isVisible = link.preview != null
                        link.preview?.let { image_right.loadImage(link.preview) }
                    }
                    "top" -> {
                        title.maxLines = 5
                        description.maxLines = 10
                        image_top.isVisible = link.preview != null
                        link.preview?.let { image_top.loadImage(link.preview) }
                    }
                    "bottom" -> {
                        title.maxLines = 5
                        description.maxLines = 10
                        image_bottom.isVisible = link.preview != null
                        link.preview?.let { image_bottom.loadImage(link.preview) }
                    }
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
            if (link.gotSelected) {
                when (settingsApi.linkImagePosition) {
                    "left" -> image_left.alpha = 0.6f
                    "right" -> image_right.alpha = 0.6f
                    "top" -> image_top.alpha = 0.6f
                    "bottom" -> image_bottom.alpha = 0.6f
                }
                title.alpha = 0.6f
                description.alpha = 0.6f
            } else {
                when (settingsApi.linkImagePosition) {
                    "left" -> image_left.alpha = 1f
                    "right" -> image_right.alpha = 1f
                    "top" -> image_top.alpha = 1f
                    "bottom" -> image_bottom.alpha = 1f
                }
                title.alpha = 1f
                description.alpha = 1f
            }
            commentsCountTextView.setOnClickListener {
                openLinkDetail()
            }
            shareTextView.setOnClickListener {
                shareUrl()
            }
        }
        containerView.setOnClickListener {
            openLinkDetail()
        }
    }

    fun openLinkDetail() {
        containerView.getActivityContext()!!.startActivity(LinkDetailsActivity.createIntent(containerView.getActivityContext()!!, link))
        if (!link.gotSelected) {
            if (settingsApi.linkSimpleList) {
                simple_image.alpha = 0.6f
                simple_title.alpha = 0.6f
            } else {
                when (settingsApi.linkImagePosition) {
                    "left" -> image_left.alpha = 0.6f
                    "right" -> image_right.alpha = 0.6f
                    "top" -> image_top.alpha = 0.6f
                    "bottom" -> image_bottom.alpha = 0.6f
                }
                title.alpha = 0.6f
                description.alpha = 0.6f
            }
            link.gotSelected = true
            linksPreferences.readLinksIds = linksPreferences.readLinksIds.plusElement("link_${link.id}")
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

    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(containerView.getActivityContext())
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }

    val url: String
        get() = "https://www.wykop.pl/link/${link.id}"
}