package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_related_layout.view.*

class RelatedWidget(context: Context, attrs: AttributeSet) : CardView(context, attrs) {
    init {
        View.inflate(context, R.layout.link_related_layout, this)
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.cardviewStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    lateinit var relatedItem : Related

    fun setRelatedData(related : Related, userManagerApi: UserManagerApi) {
        relatedItem = related
        title.text = related.title
        urlTextView.text = related.url
        userNameTextView.text = related.author.nick
        userNameTextView.setTextColor(context.getGroupColor(related.author.group))
        setOnClickListener {
            context.openBrowser(related.url)
        }
        voteCountTextView.text = if (related.voteCount > 0) "+${related.voteCount}" else "${related.voteCount}"
        authorHeaderView.setAuthor(related.author)
    }
}