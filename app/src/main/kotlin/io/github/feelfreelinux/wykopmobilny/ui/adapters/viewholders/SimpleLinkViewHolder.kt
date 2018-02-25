package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.LinkItemPresenter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.SimpleItemWidget
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.simple_link_layout.*

class SimpleLinkViewHolder(val view: SimpleItemWidget, val settingsApi: SettingsPreferencesApi, val linkItemPresenter: LinkItemPresenter) : RecyclableViewHolder(view) {
    fun bindView(link: Link) {
        view.setLinkData(link, linkItemPresenter, settingsApi)
    }

    override fun cleanRecycled() {
        view.apply {
            GlideApp.with(containerView).clear(simple_image)
            simple_title.text = null
            containerView?.setOnClickListener(null)
        }
    }
}