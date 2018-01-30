package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import android.content.Context
import android.graphics.Color
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.MonthYearPickerDialog
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_bury_menu_bottomsheet.view.*
import kotlinx.android.synthetic.main.link_details_header_layout.view.*
import kotlinx.android.synthetic.main.link_menu_bottomsheet.view.*
import java.net.URL

class LinkWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs), LinkView, URLClickedListener {

    lateinit var link : Link
    lateinit var presenter : LinkPresenter
    lateinit var userManager : UserManagerApi

    init {
        View.inflate(context, R.layout.link_details_header_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    fun setLinkData(link: Link, linkPresenter: LinkPresenter, userManagerApi: UserManagerApi) {
        this.link = link
        presenter = linkPresenter
        userManager = userManagerApi
        presenter.subscribe(this)
        presenter.linkId = link.id
        when (link.userVote) {
            "dig" ->  showDigged()
            "bury" -> showBurried()
            null -> showUnvoted()
        }
        setupHeader()
        setupButtons()
        setupBody()
    }

    private fun setupHeader() {
        dateTextView.text = link.date
        hotBadgeStrip.isVisible = link.isHot
        val author = link.author
        if (author != null) {
            avatarView.isVisible = true
            userTextView.isVisible = true
            avatarView.setAuthor(link.author!!)
            userTextView.text = link.author!!.nick
            userTextView.setTextColor(context.getGroupColor(link.author!!.group))
        } else {
            avatarView.isVisible = false
            userTextView.isVisible = false
        }
        urlTextView.text = URL(link.sourceUrl).host.removePrefix("www.")
        tagsTextView.prepareBody(link.tags.convertToTagsHtml(), this)
    }

    private fun setupButtons() {
        diggCountTextView.text = link.voteCount.toString()
        diggCountTextView.setup(userManager)
        commentsCountTextView.text = link.commentsCount.toString()
        moreOptionsTextView.setOnClickListener {
            openOptionsMenu()
        }

        shareTextView.setOnClickListener {
            shareUrl()
        }

        favoriteButton.isVisible = userManager.isUserAuthorized()
        favoriteButton.isFavorite = link.userFavorite
        favoriteButton.setOnClickListener {
            presenter.markFavorite()
        }
    }

    private fun setupBody() {
        title.text = link.title.removeHtml()
        image.isVisible = link.preview != null
        link.preview?.let { image.loadImage(link.preview!!.stripImageCompression()) }
        description.text = link.description.removeHtml()
        relatedCountTextView.text = link.relatedCount.toString()
        relatedCountTextView.setOnClickListener {
            if (link.relatedCount > 0)
            presenter.openRelatedList()
        }
        setOnClickListener {
            presenter.navigatorApi.openBrowser(link.sourceUrl)
        }
    }

    override fun showVoteCount(digResponse: DigResponse) {
        link.buryCount = digResponse.buries
        link.voteCount = digResponse.diggs
        diggCountTextView.text = digResponse.diggs.toString()
    }

    override fun showBurried() {
        link.userVote = "bury"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("bury")
        diggCountTextView.unvoteListener =  {
            presenter.voteRemove()
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
    }

    override fun showDigged() {
        link.userVote = "dig"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("dig")
        diggCountTextView.unvoteListener =  {
            presenter.voteRemove()
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
    }

    override fun showUnvoted() {
        diggCountTextView.isButtonSelected = false
        diggCountTextView.setVoteState(null)
        diggCountTextView.voteListener = {
            presenter.voteUp()
            diggCountTextView.isEnabled = false
        }
        link.userVote = null
        diggCountTextView.isEnabled = true
    }

    fun openOptionsMenu() {
        val activityContext = getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.link_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            tvDiggerList.text = resources.getString(R.string.dig_list, link.voteCount)
            tvBuryList.text = resources.getString(R.string.bury_list, link.buryCount)
            link_diggers.setOnClickListener {
                presenter.openUpvotersList()
                dialog.dismiss()
            }

            link_bury_list.setOnClickListener {
                presenter.openDownvotersList()
                dialog.dismiss()
            }

            link_bury.setOnClickListener {
                dialog.dismiss()
                openBuryReasonMenu()
            }

            link_related.setOnClickListener {
                if (link.relatedCount > 0)
                presenter.openRelatedList()
                dialog.dismiss()
            }

            link_report.setOnClickListener {
                MonthYearPickerDialog.newInstance().show((getActivityContext()!! as BaseActivity).supportFragmentManager, "monthYearPickerDialog")
                dialog.dismiss()
            }

            link_report.isVisible = userManager.isUserAuthorized()
            link_bury.isVisible = userManager.isUserAuthorized()
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    fun openBuryReasonMenu() {
        val activityContext = getActivityContext()!!
        val dialog = BottomSheetDialog(activityContext)
        val bottomSheetView = activityContext.layoutInflater.inflate(R.layout.link_bury_menu_bottomsheet, null)
        dialog.setContentView(bottomSheetView)

        bottomSheetView.apply {
            reason_duplicate.setOnClickListener {
                presenter.voteDown(LinkPresenter.BURY_REASON_DUPLICATE)
                dialog.dismiss()
            }

            reason_spam.setOnClickListener {
                presenter.voteDown(LinkPresenter.BURY_REASON_SPAM)
                dialog.dismiss()
            }

            reason_fake_info.setOnClickListener {
                presenter.voteDown(LinkPresenter.BURY_REASON_FAKE_INFO)
                dialog.dismiss()
            }

            reason_wrong_content.setOnClickListener {
                presenter.voteDown(LinkPresenter.BURY_REASON_WRONG_CONTENT)
                dialog.dismiss()
            }

            reason_unsuitable_content.setOnClickListener {
                presenter.voteDown(LinkPresenter.BURY_REASON_UNSUITABLE_CONTENT)
                dialog.dismiss()
            }
        }

        val mBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        dialog.setOnShowListener {
            mBehavior.peekHeight = bottomSheetView.height
        }
        dialog.show()
    }

    override fun handleUrl(url: String) {
        presenter.handleUrl(url)
    }

    override fun showErrorDialog(e: Throwable) {
        context.showExceptionDialog(e)
    }

    override fun markFavorite() {
        link.userFavorite = !link.userFavorite
        favoriteButton.isFavorite = link.userFavorite
    }

    fun String.convertToTagsHtml() : String {
        val html = StringBuilder()
        split(" ").forEach {
            html.append("<a href=\"$it\">$it<\\a> ")
        }
        return html.toString()
    }

    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(getActivityContext()!!)
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }

    val url : String
        get() = "https://www.wykop.pl/link/${link.id}"
}