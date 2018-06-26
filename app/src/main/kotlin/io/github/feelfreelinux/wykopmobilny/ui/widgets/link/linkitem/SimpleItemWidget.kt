package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem

import android.content.Context
import androidx.core.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse
import io.github.feelfreelinux.wykopmobilny.utils.*
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import kotlinx.android.synthetic.main.simple_link_layout.*

class SimpleItemWidget(context: Context) : BaseLinkItemWidget(context) {
    override val containerView: View? = View.inflate(context, R.layout.simple_link_layout, this)
    val diggCountDrawable by lazy {
        val typedArray = context.obtainStyledAttributes(arrayOf(
                R.attr.digCountDrawable).toIntArray())
        val selectedDrawable = typedArray.getDrawable(0)
        typedArray.recycle()
        selectedDrawable
    }

    lateinit var settingsPreferencesApi : SettingsPreferencesApi
    lateinit var linkPresenter : LinkItemPresenter

    companion object {
        fun createView(context: Context): SimpleItemWidget {
            val widget = SimpleItemWidget(context)
            widget.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            return widget
        }
    }

    init {
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
        linkPresenter = presenter
        settingsPreferencesApi = settingsPreferences
        linkPresenter.linkId = link.id
        linkPresenter.subscribe(this)
        setupBody()
    }

    fun setupBody() {
        if (link.gotSelected) {
            setWidgetAlpha(ALPHA_VISITED)
        } else {
            setWidgetAlpha(ALPHA_NEW)
        }

        when (link.userVote) {
            "dig" -> showDigged()
            "bury" -> showBurried()
            else -> showUnvoted()
        }
        simple_digg_count.text = link.voteCount.toString()
        simple_title.text = link.title
        hotBadgeStripSimple.isVisible = link.isHot
        simple_digg_hot.isVisible = link.isHot

        simple_image.isVisible = link.preview != null && settingsPreferencesApi.linkShowImage
        if (settingsPreferencesApi.linkShowImage) {
            link.preview?.let { simple_image.loadImage(link.preview!!) }
        }

        containerView?.setOnClickListener {
            openLinkDetail()
        }
    }

    override fun showBurried() {
        link.userVote = "bury"
        simple_digg.isEnabled = true
        simple_digg.background = ContextCompat.getDrawable(context, R.drawable.ic_frame_votes_buried)
        simple_digg.setOnClickListener {
            linkPresenter.userManagerApi.runIfLoggedIn(context) {
                simple_digg.isEnabled = false
                linkPresenter.voteRemove()
            }
        }
    }

    override fun showDigged() {
        link.userVote = "dig"
        simple_digg.isEnabled = true
        simple_digg.background = ContextCompat.getDrawable(context, R.drawable.ic_frame_votes_digged)
        simple_digg.setOnClickListener {
            linkPresenter.userManagerApi.runIfLoggedIn(context) {
                simple_digg.isEnabled = false
                linkPresenter.voteRemove()
            }
        }
    }

    override fun showUnvoted() {
        link.userVote = null
        simple_digg.isEnabled = true
        simple_digg.background = diggCountDrawable
        simple_digg.setOnClickListener {
            linkPresenter.userManagerApi.runIfLoggedIn(context) {
                simple_digg.isEnabled = false
                linkPresenter.voteUp()
            }
        }
    }

    override fun showVoteCount(digResponse: DigResponse) {
        link.buryCount = digResponse.buries
        link.voteCount = digResponse.diggs
        simple_digg_count.text = digResponse.diggs.toString()
    }


    override fun setWidgetAlpha(alpha: Float) {
        simple_image.alpha = alpha
        simple_title.alpha = alpha
    }
}