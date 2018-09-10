package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ShareCompat
import androidx.cardview.widget.CardView
import android.util.TypedValue
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferences
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import kotlinx.android.extensions.LayoutContainer

abstract class BaseLinkItemWidget(context: Context) : androidx.constraintlayout.widget.ConstraintLayout(context, null, R.style.EntryCardView), LinkItemView, LayoutContainer {
    override fun showErrorDialog(e: Throwable) {
        context.showExceptionDialog(e)
    }

    lateinit var link: Link

    companion object {
        val ALPHA_NEW = 1f
        val ALPHA_VISITED = 0.6f
    }

    init {
        isClickable = true
        isFocusable = true
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.itemBackgroundColorStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)
    }

    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(containerView!!.getActivityContext())
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }

    val url: String
        get() = "https://www.wykop.pl/link/${link.id}"
}