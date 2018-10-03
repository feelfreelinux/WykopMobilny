package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferences
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.link_layout.*

class LinkViewHolder(
    override val containerView: View,
    val settingsApi: SettingsPreferencesApi,
    val navigatorApi: NewNavigatorApi,
    val userManagerApi: UserManagerApi,
    private val linkActionListener: LinkActionListener
) : RecyclableViewHolder(containerView), LayoutContainer {

    companion object {
        const val ALPHA_VISITED = 0.6f
        const val ALPHA_NEW = 1f
        const val TYPE_IMAGE = 14
        const val TYPE_NOIMAGE = 15
        const val TYPE_BLOCKED = 16

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(
            parent: ViewGroup,
            viewType: Int,
            userManagerApi: UserManagerApi,
            settingsPreferencesApi: SettingsPreferencesApi,
            navigatorApi: NewNavigatorApi,
            linkActionListener: LinkActionListener
        ): LinkViewHolder {
            val view = LinkViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.link_layout, parent, false),
                settingsPreferencesApi,
                navigatorApi,
                userManagerApi,
                linkActionListener
            )
            if (viewType == TYPE_IMAGE) view.inflateCorrectImageView()
            view.type = viewType
            return view
        }

        fun getViewTypeForLink(link: Link, settingsPreferencesApi: SettingsPreferencesApi): Int {
            return if (settingsPreferencesApi.linkSimpleList) {
                SimpleLinkViewHolder.getViewTypeForLink(link)
            } else {
                if (link.isBlocked) TYPE_BLOCKED
                else if (link.preview == null || !settingsPreferencesApi.linkShowImage) TYPE_NOIMAGE
                else TYPE_IMAGE
            }
        }
    }

    var type: Int = TYPE_IMAGE
    lateinit var previewImageView: ImageView
    private val linksPreferences by lazy { LinksPreferences(containerView.context) }

    fun inflateCorrectImageView() {
        previewImageView = when (settingsApi.linkImagePosition) {
            "top" -> {
                val img = image_top.inflate() as ImageView
                if (settingsApi.linkShowAuthor) {
                    val params = img.layoutParams as ViewGroup.MarginLayoutParams
                    params.topMargin = (img.context.resources.displayMetrics.density * 8).toInt()
                }
                img
            }
            "right" -> image_right.inflate() as ImageView
            "bottom" -> image_bottom.inflate() as ImageView
            else -> image_left.inflate() as ImageView
        }
    }

    override fun cleanRecycled() {

    }

    fun bindView(link: Link) {
        setupBody(link)
        setupButtons(link)
    }

    private fun openLinkDetail(link: Link) {
        navigatorApi.openLinkDetailsActivity(link)
        if (!link.gotSelected) {
            setWidgetAlpha(ALPHA_VISITED)
            link.gotSelected = true
            linksPreferences.readLinksIds = linksPreferences.readLinksIds.plusElement("link_${link.id}")
        }
    }

    private fun setupBody(link: Link) {
        if (link.gotSelected) {
            setWidgetAlpha(ALPHA_VISITED)
        } else {
            setWidgetAlpha(ALPHA_NEW)
        }

        if (settingsApi.linkImagePosition == "left" || settingsApi.linkImagePosition == "right") {
            titleTextView.maxLines = 2
            description.maxLines = 3
        }

        if (type == TYPE_IMAGE) {
            previewImageView.loadImage(link.preview!!)

        }
        if (settingsApi.linkShowAuthor && link.author != null) {
            authorHeaderView.setAuthorData(link.author, link.date, link.app)
            authorHeaderView.isVisible = true
            dateTextView.isVisible = false
        }

        titleTextView.text = link.title
        description.text = link.description
    }

    private fun setupButtons(link: Link) {
        diggCountTextView.voteCount = link.voteCount
        diggCountTextView.setup(userManagerApi)
        diggCountTextView.setVoteState(link.userVote)
        when (link.userVote) {
            "dig" -> showDigged(link)
            "bury" -> showBurried(link)
            else -> showUnvoted(link)
        }

        commentsCountTextView.text = link.commentsCount.toString()
        dateTextView.text = link.date
        hotBadgeStrip.isVisible = link.isHot
        hotIcon.isVisible = link.isHot
        commentsCountTextView.setOnClickListener {
            openLinkDetail(link)
        }
        shareTextView.setOnClickListener {
        }
        containerView.setOnClickListener {
            openLinkDetail(link)
        }
    }

    private fun showBurried(link: Link) {
        link.userVote = "bury"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("bury")
        diggCountTextView.unvoteListener = {
            linkActionListener.dig(link)
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
    }

    private fun showDigged(link: Link) {
        link.userVote = "dig"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("dig")
        diggCountTextView.unvoteListener = {
            linkActionListener.removeVote(link)
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
    }

    private fun showUnvoted(link: Link) {
        diggCountTextView.isButtonSelected = false
        diggCountTextView.setVoteState(null)
        diggCountTextView.voteListener = {
            linkActionListener.dig(link)
            diggCountTextView.isEnabled = false
        }
        link.userVote = null
        diggCountTextView.isEnabled = true
    }

    private fun setWidgetAlpha(alpha: Float) {
        if (type == TYPE_IMAGE) previewImageView.alpha = alpha
        titleTextView.alpha = alpha
        description.alpha = alpha
    }
}