package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.drawable.Drawable
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.NotificationHeader
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.hashtag_notification_header_list_item.view.*

class NotificationHeaderViewHolder(
    override val containerView: View,
    val navigatorApi: NewNavigatorApi,
    var collapseListener: (Boolean, String) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {

    val collapseDrawable: Drawable? by lazy {
        val typedArray = containerView.context.obtainStyledAttributes(
            arrayOf(
                R.attr.collapseDrawable
            ).toIntArray()
        )
        val drawable = typedArray.getDrawable(0)
        typedArray.recycle()
        drawable
    }

    val expandDrawable: Drawable? by lazy {
        val typedArray = containerView.context.obtainStyledAttributes(
            arrayOf(
                R.attr.expandDrawable
            ).toIntArray()
        )
        val drawable = typedArray.getDrawable(0)
        typedArray.recycle()
        drawable
    }

    fun bindView(tag: NotificationHeader) {
        containerView.notificationTag.text = "#" + tag.tag
        containerView.notificationCount.text = tag.notificationsCount.toString()
        val drawable = if (tag.visible) collapseDrawable else expandDrawable
        containerView.collapseButtonImageView.setImageDrawable(drawable)
        containerView.collapseButtonImageView.setOnClickListener {
            collapseListener(!tag.visible, tag.tag)
        }
        containerView.setOnClickListener {
            navigatorApi.openTagActivity(tag.tag.removePrefix("#"))
        }
    }
}
