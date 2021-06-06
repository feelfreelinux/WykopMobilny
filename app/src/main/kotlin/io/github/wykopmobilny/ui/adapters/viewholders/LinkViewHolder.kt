package io.github.wykopmobilny.ui.adapters.viewholders

import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import io.github.wykopmobilny.databinding.LinkLayoutBinding
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.loadImage
import io.github.wykopmobilny.utils.preferences.LinksPreferences
import io.github.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.wykopmobilny.utils.usermanager.UserManagerApi

class LinkViewHolder(
    private val binding: LinkLayoutBinding,
    private val settingsApi: SettingsPreferencesApi,
    private val navigatorApi: NewNavigatorApi,
    private val userManagerApi: UserManagerApi,
    private val linkActionListener: LinkActionListener
) : RecyclableViewHolder(binding.root) {

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
                LinkLayoutBinding.inflate(parent.layoutInflater, parent, false),
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

    private var type: Int = TYPE_IMAGE
    private lateinit var previewImageView: ImageView
    private val linksPreferences by lazy { LinksPreferences(itemView.context) }

    fun inflateCorrectImageView() {
        previewImageView = when (settingsApi.linkImagePosition) {
            "top" -> {
                val img = binding.imageTop.inflate() as ImageView
                if (settingsApi.linkShowAuthor) {
                    val params = img.layoutParams as ViewGroup.MarginLayoutParams
                    params.topMargin = (img.context.resources.displayMetrics.density * 8).toInt()
                }
                img
            }
            "right" -> binding.imageRight.inflate() as ImageView
            "bottom" -> binding.imageBottom.inflate() as ImageView
            else -> binding.imageLeft.inflate() as ImageView
        }
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
            binding.titleTextView.maxLines = 2
            binding.description.maxLines = 3
        }

        if (type == TYPE_IMAGE) {
            previewImageView.loadImage(link.preview!!)
        }
        if (settingsApi.linkShowAuthor && link.author != null) {
            binding.authorHeaderView.setAuthorData(link.author, link.date, link.app)
            binding.authorHeaderView.isVisible = true
            binding.dateTextView.isVisible = false
        }

        binding.titleTextView.text = link.title
        binding.description.text = link.description
    }

    private fun setupButtons(link: Link) {
        binding.diggCountTextView.voteCount = link.voteCount
        binding.diggCountTextView.setup(userManagerApi)
        binding.diggCountTextView.setVoteState(link.userVote)
        when (link.userVote) {
            "dig" -> showDigged(link)
            "bury" -> showBurried(link)
            else -> showUnvoted(link)
        }

        binding.commentsCountTextView.text = link.commentsCount.toString()
        binding.dateTextView.text = link.date
        binding.hotBadgeStrip.isVisible = link.isHot
        binding.hotIcon.isVisible = link.isHot
        binding.commentsCountTextView.setOnClickListener {
            openLinkDetail(link)
        }
        binding.shareTextView.setOnClickListener {
        }
        itemView.setOnClickListener {
            openLinkDetail(link)
        }
    }

    private fun showBurried(link: Link) {
        link.userVote = "bury"
        binding.diggCountTextView.isButtonSelected = true
        binding.diggCountTextView.setVoteState("bury")
        binding.diggCountTextView.unvoteListener = {
            linkActionListener.dig(link)
            binding.diggCountTextView.isEnabled = false
        }
        binding.diggCountTextView.isEnabled = true
    }

    private fun showDigged(link: Link) {
        link.userVote = "dig"
        binding.diggCountTextView.isButtonSelected = true
        binding.diggCountTextView.setVoteState("dig")
        binding.diggCountTextView.unvoteListener = {
            linkActionListener.removeVote(link)
            binding.diggCountTextView.isEnabled = false
        }
        binding.diggCountTextView.isEnabled = true
    }

    private fun showUnvoted(link: Link) {
        binding.diggCountTextView.isButtonSelected = false
        binding.diggCountTextView.setVoteState(null)
        binding.diggCountTextView.voteListener = {
            linkActionListener.dig(link)
            binding.diggCountTextView.isEnabled = false
        }
        link.userVote = null
        binding.diggCountTextView.isEnabled = true
    }

    private fun setWidgetAlpha(alpha: Float) {
        if (type == TYPE_IMAGE) previewImageView.alpha = alpha
        binding.titleTextView.alpha = alpha
        binding.description.alpha = alpha
    }
}
