package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.link.LinkHeaderActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.link.LinkInteractor
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.link_bury_menu_bottomsheet.view.*
import kotlinx.android.synthetic.main.link_details_header_layout.*
import kotlinx.android.synthetic.main.link_menu_bottomsheet.view.*
import java.net.URL

class LinkHeaderViewHolder(
    override val containerView: View,
    private val linkActionListener: LinkHeaderActionListener,
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi,
    val userManagerApi: UserManagerApi
) : RecyclableViewHolder(containerView), LayoutContainer {

    companion object {
        const val TYPE_HEADER = 64

        /**
         * Inflates correct view (with embed, survey or both) depending on viewType
         */
        fun inflateView(
            parent: ViewGroup,
            userManagerApi: UserManagerApi,
            navigatorApi: NewNavigatorApi,
            linkHandlerApi: WykopLinkHandlerApi,
            linkHeaderActionListener: LinkHeaderActionListener
        ): LinkHeaderViewHolder {
            return LinkHeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.link_details_header_layout, parent, false),
                linkHeaderActionListener,
                navigatorApi,
                linkHandlerApi,
                userManagerApi
            )
        }
    }

    fun bindView(link: Link) {
        when (link.userVote) {
            "dig" -> showDigged(link)
            "bury" -> showBurried(link)
            null -> showUnvoted(link)
        }
        setupHeader(link)
        setupButtons(link)
        setupBody(link)
    }

    private fun setupHeader(link: Link) {
        dateTextView.text = link.date
        hotBadgeStrip.isVisible = link.isHot
        val author = link.author
        if (author != null) {
            avatarView.isVisible = true
            userTextView.isVisible = true
            userTextView.setOnClickListener { navigatorApi.openProfileActivity(link.author.nick) }
            avatarView.setOnClickListener { navigatorApi.openProfileActivity(link.author.nick) }
            avatarView.setAuthor(link.author)
            userTextView.text = link.author.nick
            userTextView.setTextColor(containerView.context.getGroupColor(link.author.group))
        } else {
            avatarView.isVisible = false
            userTextView.isVisible = false
        }

        urlTextView.text = URL(link.sourceUrl).host.removePrefix("www.")
        blockedTextView.prepareBody(link.tags.convertToTagsHtml()) { linkHandlerApi.handleUrl(it) }
    }

    private fun setupButtons(link: Link) {
        diggCountTextView.text = link.voteCount.toString()
        diggCountTextView.setup(userManagerApi)
        commentsCountTextView.text = link.commentsCount.toString()
        moreOptionsTextView.setOnClickListener {
            openOptionsMenu(link)
        }
        shareTextView.setOnClickListener {
            navigatorApi.shareUrl(link.url)
        }
        commentsCountTextView.setOnClickListener {}

        favoriteButton.isVisible = userManagerApi.isUserAuthorized()
        favoriteButton.isFavorite = link.userFavorite
        favoriteButton.setOnClickListener {
            linkActionListener.markFavorite(link)
        }
    }

    private fun setupBody(link: Link) {
        titleTextView.text = link.title.removeHtml()
        image.isVisible = link.preview != null
        link.preview?.let { image.loadImage(link.preview.stripImageCompression()) }
        description.text = link.description.removeHtml()
        relatedCountTextView.text = link.relatedCount.toString()
        relatedCountTextView.setOnClickListener {
            navigatorApi.openLinkRelatedActivity(link.id)
        }
        containerView.setOnClickListener {
            linkHandlerApi.handleUrl(link.sourceUrl)
        }
    }

    private fun showBurried(link: Link) {
        link.userVote = "bury"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("bury")
        diggCountTextView.unvoteListener = {
            linkActionListener.removeVote(link)
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
            linkActionListener.digLink(link)
            diggCountTextView.isEnabled = false
        }
        link.userVote = null
        diggCountTextView.isEnabled = true
    }

    private fun openOptionsMenu(link: Link) {
        val activityContext = containerView.getActivityContext()!!
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.link_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            tvDiggerList.text = resources.getString(R.string.dig_list, link.voteCount)
            tvBuryList.text = resources.getString(R.string.bury_list, link.buryCount)
            link_diggers.setOnClickListener {
                navigatorApi.openLinkUpvotersActivity(link.id)
                dialog.dismiss()
            }

            link_bury_list.setOnClickListener {
                navigatorApi.openLinkDownvotersActivity(link.id)
                dialog.dismiss()
            }

            link_bury.setOnClickListener {
                dialog.dismiss()
                openBuryReasonMenu(link)
            }

            link_related.setOnClickListener {
                navigatorApi.openLinkRelatedActivity(link.id)
                dialog.dismiss()
            }
            link_bury.isVisible = userManagerApi.isUserAuthorized()
        }

        val mBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    private fun openBuryReasonMenu(link: Link) {
        val activityContext = containerView.getActivityContext()!!
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.link_bury_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            reason_duplicate.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_DUPLICATE)
                dialog.dismiss()
            }

            reason_spam.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_SPAM)
                dialog.dismiss()
            }

            reason_fake_info.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_FAKE_INFO)
                dialog.dismiss()
            }

            reason_wrong_content.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_WRONG_CONTENT)
                dialog.dismiss()
            }

            reason_unsuitable_content.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_UNSUITABLE_CONTENT)
                dialog.dismiss()
            }
        }

        val mBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    private fun String.convertToTagsHtml(): String {
        val html = StringBuilder()
        split(" ").forEach {
            html.append("<a href=\"$it\">$it<\\a> ")
        }
        return html.toString()
    }
}