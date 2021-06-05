package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.databinding.SimpleLinkLayoutBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.layoutInflater
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferences
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.extensions.LayoutContainer

class SimpleLinkViewHolder(
    private val binding: SimpleLinkLayoutBinding,
    private val settingsApi: SettingsPreferencesApi,
    private val navigatorApi: NewNavigatorApi,
    private val userManagerApi: UserManagerApi,
    private val linkActionListener: LinkActionListener
) : RecyclableViewHolder(binding.root), LayoutContainer {

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
        ) = SimpleLinkViewHolder(
            SimpleLinkLayoutBinding.inflate(parent.layoutInflater, parent, false),
            settingsPreferencesApi,
            navigatorApi,
            userManagerApi,
            linkActionListener
        )

        fun getViewTypeForLink(link: Link): Int {
            return if (link.isBlocked) TYPE_BLOCKED
            else TYPE_SIMPLE_LINK
        }
    }

    override val containerView = binding.root

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
        binding.simpleDiggCount.text = link.voteCount.toString()
        binding.simpleTitle.text = link.title
        binding.hotBadgeStripSimple.isVisible = link.isHot
        binding.simpleDiggHot.isVisible = link.isHot

        binding.simpleImage.isVisible = link.preview != null && settingsApi.linkShowImage
        if (settingsApi.linkShowImage) {
            link.preview?.let { binding.simpleImage.loadImage(link.preview) }
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
        binding.simpleDigg.isEnabled = true
        binding.simpleDigg.background = ContextCompat.getDrawable(containerView.context, R.drawable.ic_frame_votes_buried)
        binding.simpleDigg.setOnClickListener {
            userManagerApi.runIfLoggedIn(containerView.context) {
                binding.simpleDigg.isEnabled = false
                linkActionListener.removeVote(link)
            }
        }
    }

    private fun showDigged(link: Link) {
        link.userVote = "dig"
        binding.simpleDigg.isEnabled = true
        binding.simpleDigg.background = ContextCompat.getDrawable(containerView.context, R.drawable.ic_frame_votes_digged)
        binding.simpleDigg.setOnClickListener {
            userManagerApi.runIfLoggedIn(containerView.context) {
                binding.simpleDigg.isEnabled = false
                linkActionListener.removeVote(link)
            }
        }
    }

    private fun showUnvoted(link: Link) {
        link.userVote = null
        binding.simpleDigg.isEnabled = true
        binding.simpleDigg.background = digCountDrawable
        binding.simpleDigg.setOnClickListener {
            userManagerApi.runIfLoggedIn(containerView.context) {
                binding.simpleDigg.isEnabled = false
                linkActionListener.dig(link)
            }
        }
    }

    private fun setWidgetAlpha(alpha: Float) {
        binding.simpleImage.alpha = alpha
        binding.simpleTitle.alpha = alpha
    }
}
