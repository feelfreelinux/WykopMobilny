package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.link_layout.*

class LinkItemWidget(context: Context) : BaseLinkItemWidget(context) {
    override val containerView: View? = View.inflate(context, R.layout.link_layout, this)

    lateinit var settingsPreferencesApi : SettingsPreferencesApi
    lateinit var linkPresenter : LinkItemPresenter

    companion object {
        fun createView(context: Context): LinkItemWidget {
            val widget = LinkItemWidget(context)
            widget.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            return widget
        }
    }

    init {
        cardElevation = resources.getDimension(R.dimen.cardview_elevation)
        radius = resources.getDimension(R.dimen.cardview_radius)
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.itemBackgroundColorStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (::linkPresenter.isInitialized)
            linkPresenter.subscribe(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        linkPresenter.unsubscribe()
    }

    fun setLinkData(linkData : Link, presenter : LinkItemPresenter, settingsPreferences: SettingsPreferencesApi) {
        link = linkData
        settingsPreferencesApi = settingsPreferences
        linkPresenter = presenter
        linkPresenter.linkId = link.id
        linkPresenter.subscribe(this)
        setupBody()
        setupButtons()
    }

    fun setupBody() {
        if (link.gotSelected) {
            setWidgetAlpha(ALPHA_VISITED)
        } else {
            setWidgetAlpha(ALPHA_NEW)
        }

        if (settingsPreferencesApi.linkImagePosition == "top" || settingsPreferencesApi.linkImagePosition == "bottom") {
            title.maxLines = 5
            description.maxLines = 10
        }
        val correctImage = getCorrectImageView()
        correctImage.isVisible = link.preview != null && settingsPreferencesApi.linkShowImage
        if (settingsPreferencesApi.linkShowImage) {
            correctImage.apply {
                isVisible = link.preview != null
                link.preview?.let { loadImage(link.preview!!) }
            }
        }
        if (settingsPreferencesApi.linkShowAuthor) {
            authorHeaderView.setAuthorData(link.author!!, link.date, link.app)
            authorHeaderView.isVisible = true
            lineTop.isVisible = true
            dateTextView.isVisible = false
        }

        title.text = link.title
        description.text = link.description
    }

    fun setupButtons() {
        diggCountTextView.voteCount = link.voteCount
        diggCountTextView.setup(linkPresenter.userManagerApi)
        when (link.userVote) {
            "dig" -> showDigged()
            "bury" -> showBurried()
            else -> showUnvoted()
        }

        commentsCountTextView.text = link.commentsCount.toString()
        dateTextView.text = link.date
        hotBadgeStrip.isVisible = link.isHot
        commentsCountTextView.setOnClickListener {
            openLinkDetail()
        }
        shareTextView.setOnClickListener {
            shareUrl()
        }
        containerView?.setOnClickListener {
            openLinkDetail()
        }
    }

    override fun showBurried() {
        link.userVote = "bury"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("bury")
        diggCountTextView.unvoteListener =  {
            linkPresenter.voteRemove()
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
    }

    override fun showDigged() {
        link.userVote = "dig"
        diggCountTextView.isButtonSelected = true
        diggCountTextView.setVoteState("dig")
        diggCountTextView.unvoteListener =  {
            linkPresenter.voteRemove()
            diggCountTextView.isEnabled = false
        }
        diggCountTextView.isEnabled = true
    }

    override fun showUnvoted() {
        diggCountTextView.isButtonSelected = false
        diggCountTextView.setVoteState(null)
        diggCountTextView.voteListener = {
            linkPresenter.voteUp()
            diggCountTextView.isEnabled = false
        }
        link.userVote = null
        diggCountTextView.isEnabled = true
    }

    override fun showVoteCount(digResponse: DigResponse) {
        link.buryCount = digResponse.buries
        link.voteCount = digResponse.diggs
        diggCountTextView.text = digResponse.diggs.toString()
    }

    fun getCorrectImageView(): ImageView {
        return when (settingsPreferencesApi.linkImagePosition) {
            "top" -> image_top
            "right" -> image_right
            "bottom" -> image_bottom
            else -> image_left
        }
    }

    override fun setWidgetAlpha(alpha: Float) {
        getCorrectImageView().alpha = alpha
        title.alpha = alpha
        description.alpha = alpha
    }
}