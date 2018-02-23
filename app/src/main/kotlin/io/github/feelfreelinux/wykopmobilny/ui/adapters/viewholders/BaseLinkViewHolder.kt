package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails.LinkDetailsActivity
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.extensions.LayoutContainer

abstract class BaseLinkViewHolder(override val containerView: View, val settingsApi: SettingsPreferencesApi) : RecyclableViewHolder(containerView), LayoutContainer {
    private val linksPreferences by lazy { LinksPreferences(containerView.context) }
    lateinit var link: Link

    companion object {
        val ALPHA_NEW = 1f
        val ALPHA_VISITED = 0.6f
    }

    open fun bindView(link: Link) {
        this.link = link
        if (link.gotSelected) {
            setWidgetAlpha(ALPHA_VISITED)
        } else {
            setWidgetAlpha(ALPHA_NEW)
        }
    }

    abstract fun setWidgetAlpha(alpha : Float)

    fun openLinkDetail() {
        containerView.getActivityContext()!!.startActivity(LinkDetailsActivity.createIntent(containerView.getActivityContext()!!, link))
        if (!link.gotSelected) {
            setWidgetAlpha(ALPHA_VISITED)
            link.gotSelected = true
            linksPreferences.readLinksIds = linksPreferences.readLinksIds.plusElement("link_${link.id}")
        }
    }


    fun shareUrl() {
        ShareCompat.IntentBuilder
                .from(containerView.getActivityContext())
                .setType("text/plain")
                .setChooserTitle(R.string.share)
                .setText(url)
                .startChooser()
    }

    val url: String
        get() = "https://www.wykop.pl/link/${link.id}"
}