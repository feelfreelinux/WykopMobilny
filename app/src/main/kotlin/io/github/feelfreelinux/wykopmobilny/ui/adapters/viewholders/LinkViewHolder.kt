package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders


import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.LinkItemPresenter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem.LinkItemWidget
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.link_layout.*

class LinkViewHolder(val view: LinkItemWidget, val settingsApi: SettingsPreferencesApi, val linkItemPresenter: LinkItemPresenter) : RecyclableViewHolder(view) {
    fun bindView(link: Link) {
        view.setLinkData(link, linkItemPresenter, settingsApi)
    }


    override fun cleanRecycled() {
        view.apply {
            title.text = null
            description.text = null
            diggCountTextView.text = null
            commentsCountTextView.text = null
            setOnClickListener(null)
            GlideApp.with(this).clear(image_left)
            GlideApp.with(this).clear(image_right)
            GlideApp.with(this).clear(image_top)
            GlideApp.with(this).clear(image_bottom)
        }
    }
}