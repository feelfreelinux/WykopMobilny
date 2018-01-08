package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.textview.URLClickedListener
import io.github.feelfreelinux.wykopmobilny.utils.textview.prepareBody
import kotlinx.android.synthetic.main.link_comment_layout.view.*
import kotlin.math.absoluteValue

class LinkCommentWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs), URLClickedListener {
    lateinit var comment : LinkComment
    lateinit var navigator : NewNavigatorApi
    lateinit var settingsApi : SettingsPreferencesApi
    init {
        View.inflate(context, R.layout.link_comment_layout, this)
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

    fun setLinkCommentData(linkComment: LinkComment, newNavigatorApi: NewNavigatorApi, settingsPreferencesApi: SettingsPreferencesApi) {
        comment = linkComment
        navigator = newNavigatorApi
        settingsApi = settingsPreferencesApi
        setupHeader()
        setupBody()
        setupButtons()
    }

    private fun setupHeader() {
        authorHeaderView.setAuthorData(comment.author, comment.date, comment.app)
    }

    private fun setupButtons() {
        plusButton.text = comment.voteCountPlus.toString()
        minusButton.text = comment.voteCountMinus.absoluteValue.toString()

    }

    private fun setupBody() {
        commentImageView.setEmbed(comment.embed, settingsApi, navigator)
        comment.body?.let {
            commentContentTextView.prepareBody(comment.body!!, this)
        }
        val margin = if (comment.id != comment.parentId) 8f else 0f
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, resources.displayMetrics)
        val params = layoutParams as MarginLayoutParams
        params.setMargins(px.toInt(), params.topMargin, params.rightMargin, params.bottomMargin)
        requestLayout()

    }

    override fun handleUrl(url: String) {
    }

}