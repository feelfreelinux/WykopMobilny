package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import android.content.Context
import android.graphics.Color
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.dialog_voters.view.*
import kotlinx.android.synthetic.main.link_bury_menu_bottomsheet.view.*
import kotlinx.android.synthetic.main.link_menu_bottomsheet.view.*
import kotlinx.android.synthetic.main.link_details_header_layout.view.*
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
        dateTextView.text = link.date.toPrettyDate()
        hotBadgeStrip.isVisible = link.isHot
        avatarView.setAuthor(link.author!!)
        dateTextView.text = link.date.toPrettyDate()
        urlTextView.text = URL(link.sourceUrl).host
        userTextView.text = link.author!!.nick
        userTextView.setTextColor(context.getGroupColor(link.author!!.group))
        tagsTextView.prepareBody(link.tags.convertToTagsHtml(), this)

    }

    private fun setupButtons() {
        diggCountTextView.text = link.voteCount.toString()
        commentsCountTextView.text = link.commentsCount.toString()
        moreOptionsTextView.setOnClickListener {
            openOptionsMenu()
        }
    }

    private fun setupBody() {
        title.text = link.title.removeHtml()
        image.isVisible = link.preview != null
        link.preview?.let { image.loadImage(link.preview!!.stripImageCompression()) }
        description.text = link.description.removeHtml()
        setOnClickListener {
            presenter.navigatorApi.openBrowser(link.sourceUrl)
        }
    }

    fun setWykopColorDrawable(isButtonSelected : Boolean) {
        if (isButtonSelected) {
            diggCountTextView.setTextColor(Color.WHITE)
            diggCountTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_wypok_activ, 0, 0, 0);
        } else {
            diggCountTextView.setTextColor(description.currentTextColor)
            val typedArray = context.obtainStyledAttributes(arrayOf(
                    R.attr.wypokDrawable).toIntArray())
            diggCountTextView.setCompoundDrawablesWithIntrinsicBounds(typedArray.getDrawable(0), null, null, null)
            typedArray.recycle()

        }
    }

    override fun showVoteCount(digResponse: DigResponse) {
        link.buryCount = digResponse.buries
        link.voteCount = digResponse.diggs
        diggCountTextView.text = digResponse.diggs.toString()
    }

    override fun showBurried() {
        diggCountTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.minusPressedColor))
        link.userVote = "bury"
        diggCountTextView.isEnabled = true
        diggCountTextView.setOnClickListener {
            presenter.voteRemove()
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
        setWykopColorDrawable(true)
    }

    override fun showDigged() {
        diggCountTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.plusPressedColor))
        link.userVote = "dig"
        diggCountTextView.isEnabled = true
        diggCountTextView.setOnClickListener {
            presenter.voteRemove()
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
        setWykopColorDrawable(true)
    }

    override fun showUnvoted() {
        diggCountTextView.setBackgroundColor(Color.TRANSPARENT)
        diggCountTextView.setOnClickListener {
            presenter.voteUp()
            diggCountTextView.isEnabled = false
        }
        link.userVote = null
        diggCountTextView.isEnabled = true
        setWykopColorDrawable(false)
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
                dialog.dismiss()
            }

            link_report.setOnClickListener {
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

    fun String.convertToTagsHtml() : String {
        val html = StringBuilder()
        split(" ").forEach {
            html.append("<a href=\"$it\">$it<\\a> ")
        }
        return html.toString()
    }
}