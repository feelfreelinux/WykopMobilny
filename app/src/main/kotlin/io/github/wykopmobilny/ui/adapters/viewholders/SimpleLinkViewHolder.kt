package io.github.wykopmobilny.ui.adapters.viewholders

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.view.isVisible
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.SimpleLinkLayoutBinding
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.storage.api.LinksPreferencesApi
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.loadImage
import io.github.wykopmobilny.utils.usermanager.UserManagerApi

class SimpleLinkViewHolder(
    private val binding: SimpleLinkLayoutBinding,
    private val settingsApi: SettingsPreferencesApi,
    private val navigatorApi: NewNavigatorApi,
    private val userManagerApi: UserManagerApi,
    private val linkActionListener: LinkActionListener,
    private val linksPreferences: LinksPreferencesApi,
) : RecyclableViewHolder(binding.root) {

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
            linkActionListener: LinkActionListener,
            linksPreferences: LinksPreferencesApi,
        ) = SimpleLinkViewHolder(
            binding = SimpleLinkLayoutBinding.inflate(parent.layoutInflater, parent, false),
            settingsApi = settingsPreferencesApi,
            navigatorApi = navigatorApi,
            userManagerApi = userManagerApi,
            linkActionListener = linkActionListener,
            linksPreferences = linksPreferences,
        )

        fun getViewTypeForLink(link: Link): Int =
            if (link.isBlocked) {
                TYPE_BLOCKED
            } else {
                TYPE_SIMPLE_LINK
            }
    }

    private val digCountDrawable by lazy {
        itemView.context.obtainStyledAttributes(arrayOf(R.attr.digCountDrawable).toIntArray())
            .use { it.getDrawable(0) }
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

        itemView.setOnClickListener {
            navigatorApi.openLinkDetailsActivity(link)
            if (!link.gotSelected) {
                setWidgetAlpha(ALPHA_VISITED)
                link.gotSelected = true
                linksPreferences.readLinksIds = linksPreferences.readLinksIds.plusElement("link_${link.id}")
            }
        }
    }

    private fun showBurried(link: Link) {
        link.userVote = "bury"
        binding.simpleDigg.isEnabled = true
        binding.simpleDigg.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_frame_votes_buried)
        binding.simpleDigg.setOnClickListener {
            userManagerApi.runIfLoggedIn(itemView.context) {
                binding.simpleDigg.isEnabled = false
                linkActionListener.removeVote(link)
            }
        }
    }

    private fun showDigged(link: Link) {
        link.userVote = "dig"
        binding.simpleDigg.isEnabled = true
        binding.simpleDigg.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_frame_votes_digged)
        binding.simpleDigg.setOnClickListener {
            userManagerApi.runIfLoggedIn(itemView.context) {
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
            userManagerApi.runIfLoggedIn(itemView.context) {
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
