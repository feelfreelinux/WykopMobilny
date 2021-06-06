package io.github.wykopmobilny.ui.adapters.viewholders

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.LinkBuryMenuBottomsheetBinding
import io.github.wykopmobilny.databinding.LinkDetailsHeaderLayoutBinding
import io.github.wykopmobilny.databinding.LinkMenuBottomsheetBinding
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.fragments.link.LinkHeaderActionListener
import io.github.wykopmobilny.ui.fragments.link.LinkInteractor
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.api.getGroupColor
import io.github.wykopmobilny.utils.api.stripImageCompression
import io.github.wykopmobilny.utils.getActivityContext
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.loadImage
import io.github.wykopmobilny.utils.textview.prepareBody
import io.github.wykopmobilny.utils.textview.removeHtml
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi
import java.net.URL

class LinkHeaderViewHolder(
    private val binding: LinkDetailsHeaderLayoutBinding,
    private val linkActionListener: LinkHeaderActionListener,
    val navigatorApi: NewNavigatorApi,
    val linkHandlerApi: WykopLinkHandlerApi,
    val userManagerApi: UserManagerApi
) : RecyclableViewHolder(binding.root) {

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
                LinkDetailsHeaderLayoutBinding.inflate(parent.layoutInflater, parent, false),
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
        binding.dateTextView.text = link.date
        binding.hotBadgeStrip.isVisible = link.isHot
        val author = link.author
        if (author != null) {
            binding.avatarView.isVisible = true
            binding.userTextView.isVisible = true
            binding.userTextView.setOnClickListener { navigatorApi.openProfileActivity(link.author.nick) }
            binding.avatarView.setOnClickListener { navigatorApi.openProfileActivity(link.author.nick) }
            binding.avatarView.setAuthor(link.author)
            binding.userTextView.text = link.author.nick
            binding.userTextView.setTextColor(itemView.context.getGroupColor(link.author.group))
        } else {
            binding.avatarView.isVisible = false
            binding.userTextView.isVisible = false
        }

        binding.urlTextView.text = URL(link.sourceUrl).host.removePrefix("www.")
        binding.blockedTextView.prepareBody(link.tags.convertToTagsHtml()) {
            linkHandlerApi.handleUrl(
                it
            )
        }
    }

    private fun setupButtons(link: Link) {
        binding.diggCountTextView.text = link.voteCount.toString()
        binding.diggCountTextView.setup(userManagerApi)
        binding.commentsCountTextView.text = link.commentsCount.toString()
        binding.moreOptionsTextView.setOnClickListener {
            openOptionsMenu(link)
        }
        binding.shareTextView.setOnClickListener {
            navigatorApi.shareUrl(link.url)
        }
        binding.commentsCountTextView.setOnClickListener {}

        binding.favoriteButton.isVisible = userManagerApi.isUserAuthorized()
        binding.favoriteButton.isFavorite = link.userFavorite
        binding.favoriteButton.setOnClickListener {
            linkActionListener.markFavorite(link)
        }
    }

    private fun setupBody(link: Link) {
        binding.titleTextView.text = link.title.removeHtml()
        binding.image.isVisible = link.preview != null
        link.preview?.let { binding.image.loadImage(link.preview.stripImageCompression()) }
        binding.description.text = link.description.removeHtml()
        binding.relatedCountTextView.text = link.relatedCount.toString()
        binding.relatedCountTextView.setOnClickListener {
            navigatorApi.openLinkRelatedActivity(link.id)
        }
        itemView.setOnClickListener {
            linkHandlerApi.handleUrl(link.sourceUrl)
        }
    }

    private fun showBurried(link: Link) {
        link.userVote = "bury"
        binding.diggCountTextView.isButtonSelected = true
        binding.diggCountTextView.setVoteState("bury")
        binding.diggCountTextView.unvoteListener = {
            linkActionListener.removeVote(link)
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
            linkActionListener.digLink(link)
            binding.diggCountTextView.isEnabled = false
        }
        link.userVote = null
        binding.diggCountTextView.isEnabled = true
    }

    private fun openOptionsMenu(link: Link) {
        val activityContext = itemView.getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = LinkMenuBottomsheetBinding.inflate(activityContext.layoutInflater)
        dialog.setContentView(bottomSheetView.root)

        bottomSheetView.apply {
            tvDiggerList.text = root.resources.getString(R.string.dig_list, link.voteCount)
            tvBuryList.text = root.resources.getString(R.string.bury_list, link.buryCount)
            linkDiggers.setOnClickListener {
                navigatorApi.openLinkUpvotersActivity(link.id)
                dialog.dismiss()
            }

            linkBuryList.setOnClickListener {
                navigatorApi.openLinkDownvotersActivity(link.id)
                dialog.dismiss()
            }

            linkBury.setOnClickListener {
                dialog.dismiss()
                openBuryReasonMenu(link)
            }

            linkRelated.setOnClickListener {
                navigatorApi.openLinkRelatedActivity(link.id)
                dialog.dismiss()
            }
            linkBury.isVisible = userManagerApi.isUserAuthorized()
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.root.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.root.height
        }
        dialog.show()
    }

    private fun openBuryReasonMenu(link: Link) {
        val activityContext = itemView.getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = LinkBuryMenuBottomsheetBinding.inflate(activityContext.layoutInflater)
        dialog.setContentView(bottomSheetView.root)

        bottomSheetView.apply {
            reasonDuplicate.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_DUPLICATE)
                dialog.dismiss()
            }

            reasonSpam.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_SPAM)
                dialog.dismiss()
            }

            reasonFakeInfo.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_FAKE_INFO)
                dialog.dismiss()
            }

            reasonWrongContent.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_WRONG_CONTENT)
                dialog.dismiss()
            }

            reasonUnsuitableContent.setOnClickListener {
                linkActionListener.buryLink(link, LinkInteractor.BURY_REASON_UNSUITABLE_CONTENT)
                dialog.dismiss()
            }
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.root.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.root.height
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
