package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.link_layout.view.*

class LinkWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    lateinit var link : Link
    init {
        View.inflate(context, R.layout.link_details_header_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    fun setLinkData(link: Link) {
        this.link = link
        setupHeader()
        setupButtons()
        setupBody()

    }

    private fun setupHeader() {
        dateTextView.text = link.date.toPrettyDate()
        hotBadgeStrip.isVisible = link.isHot
    }

    private fun setupButtons() {
        diggCountTextView.text = link.voteCount.toString()
        commentsCountTextView.text = link.commentsCount.toString()
    }

    private fun setupBody() {
        title.text = link.title.removeHtml()
        image.isVisible = link.preview != null
        link.preview?.let { image.loadImage(link.preview!!.stripImageCompression()) }
        description.text = link.description.removeHtml()
        setOnClickListener {
            NewNavigator(getActivityContext()!!).openBrowser(link.sourceUrl)
        }
    }

}