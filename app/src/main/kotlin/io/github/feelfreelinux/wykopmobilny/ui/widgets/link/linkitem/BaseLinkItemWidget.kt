package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem

import android.content.Context
import android.support.v4.app.ShareCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BaseLinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.LinksPreferences
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import kotlinx.android.extensions.LayoutContainer

abstract class BaseLinkItemWidget(context: Context) : CardView(context, null, R.style.EntryCardView), LinkItemView, LayoutContainer {
    override fun showErrorDialog(e: Throwable) {
        context.showExceptionDialog(e)
    }

    private val linksPreferences by lazy { LinksPreferences(containerView!!.context) }
    lateinit var link: Link

    companion object {
        val ALPHA_NEW = 1f
        val ALPHA_VISITED = 0.6f
    }
    abstract fun setWidgetAlpha(alpha : Float)

    fun openLinkDetail() {
        containerView?.getActivityContext()!!.startActivity(LinkDetailsActivity.createIntent(containerView!!.getActivityContext()!!, link))
        if (!link.gotSelected) {
            setWidgetAlpha(ALPHA_VISITED)
            link.gotSelected = true
            linksPreferences.readLinksIds = linksPreferences.readLinksIds.plusElement("link_${link.id}")
        }
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