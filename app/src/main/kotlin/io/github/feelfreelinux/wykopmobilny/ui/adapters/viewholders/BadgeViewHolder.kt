package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate
import kotlinx.android.synthetic.main.badge_list_item.view.*

class BadgeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(badge : BadgeResponse) {
        view.badgeTitle.text = badge.name
        view.badgeImg.loadImage(badge.icon)
        view.description.text = badge.description
        view.date.text = badge.date.toPrettyDate()
    }
}