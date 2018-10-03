package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.simple_link_layout.*

class SimpleLinkViewHolder(
    override val containerView: View,
    val settingsApi: SettingsPreferencesApi,
    val navigatorApi: NewNavigatorApi,
    val userManagerApi: UserManagerApi,
    private val linkActionListener: LinkActionListener
) : RecyclableViewHolder(containerView), LayoutContainer {

    companion object {
        const val ALPHA_NEW = 1f
        const val ALPHA_VISITED = 0.6f
        const val TYPE_SIMPLE_LINK = 77
        const val TYPE_BLOCKED = 78

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(
            parent: ViewGroup,
            userManagerApi: UserManagerApi,
            settingsPreferencesApi: SettingsPreferencesApi,
            navigatorApi: NewNavigatorApi,
            linkActionListener: LinkActionListener
        ): SimpleLinkViewHolder {
            return SimpleLinkViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.simple_link_layout, parent, false),
                settingsPreferencesApi,
                navigatorApi,
                userManagerApi,
                linkActionListener
            )
        }

        fun getViewTypeForLink(link: Link): Int {
            return if (link.isBlocked) TYPE_BLOCKED
            else TYPE_SIMPLE_LINK
        }
    }

    private val linkPreferences by lazy { LinksPreferences(containerView.context) }
    private val digCountDrawable by lazy {
        val typedArray = containerView.context.obtainStyledAttributes(
            arrayOf(
                R.attr.digCountDrawable
            ).toIntArray()
        )
        val selectedDrawable = typedArray.getDrawable(0)
        typedArray.recycle()
        selectedDrawable
    }

    fun bindView(link: Link) {
        setupBody(link)
    }

    private fun setupBody(link: Link) {
        if (link.gotSelected) {
            setWidgetAlpha(ALPHA_VISITED)
        } else {
            setWidgetAlpha(ALPHA_NEW)
        }

        when (link.userVote) {
            "dig" -> showDigged(link)
            "bury" -> showBurried(link)
            else -> showUnvoted(link)
        }
        simple_digg_count.text = link.voteCount.toString()
        simple_title.text = link.title
        hotBadgeStripSimple.isVisible = link.isHot
        simple_digg_hot.isVisible = link.isHot

        simple_image.isVisible = link.preview != null && settingsApi.linkShowImage
        if (settingsApi.linkShowImage) {
            link.preview?.let { simple_image.loadImage(link.preview) }
        }

        containerView.setOnClickListener {
            navigatorApi.openLinkDetailsActivity(link)
            if (!link.gotSelected) {
                setWidgetAlpha(ALPHA_VISITED)
                link.gotSelected = true
                linkPreferences.readLinksIds = linkPreferences.readLinksIds.plusElement("link_${link.id}")
            }
        }
    }

    private fun showBurried(link: Link) {
        link.userVote = "bury"
        simple_digg.isEnabled = true
        simple_digg.background = ContextCompat.getDrawable(containerView.context, R.drawable.ic_frame_votes_buried)
        simple_digg.setOnClickListener {
            userManagerApi.runIfLoggedIn(containerView.context) {
                simple_digg.isEnabled = false
                linkActionListener.removeVote(link)
            }
        }
    }

    private fun showDigged(link: Link) {
        link.userVote = "dig"
        simple_digg.isEnabled = true
        simple_digg.background = ContextCompat.getDrawable(containerView.context, R.drawable.ic_frame_votes_digged)
        simple_digg.setOnClickListener {
            userManagerApi.runIfLoggedIn(containerView.context) {
                simple_digg.isEnabled = false
                linkActionListener.removeVote(link)
            }
        }
    }

    private fun showUnvoted(link: Link) {
        link.userVote = null
        simple_digg.isEnabled = true
        simple_digg.background = digCountDrawable
        simple_digg.setOnClickListener {
            userManagerApi.runIfLoggedIn(containerView.context) {
                simple_digg.isEnabled = false
                linkActionListener.dig(link)
            }
        }
    }

    private fun setWidgetAlpha(alpha: Float) {
        simple_image.alpha = alpha
        simple_title.alpha = alpha
    }

    override fun cleanRecycled() {
        //Do nothing
    }
}