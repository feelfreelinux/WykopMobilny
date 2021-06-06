package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.graphics.drawable.Drawable
import androidx.core.content.res.use
import androidx.recyclerview.widget.RecyclerView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.databinding.HashtagNotificationHeaderListItemBinding
import io.github.feelfreelinux.wykopmobilny.models.dataclass.NotificationHeader
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi

class NotificationHeaderViewHolder(
    private val binding: HashtagNotificationHeaderListItemBinding,
    private val navigatorApi: NewNavigatorApi,
    private val collapseListener: (Boolean, String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    val collapseDrawable: Drawable? by lazy {
        itemView.context.obtainStyledAttributes(arrayOf(R.attr.collapseDrawable).toIntArray())
            .use { it.getDrawable(0) }
    }

    val expandDrawable: Drawable? by lazy {
        itemView.context.obtainStyledAttributes(arrayOf(R.attr.expandDrawable).toIntArray())
            .use { it.getDrawable(0) }
    }

    fun bindView(tag: NotificationHeader) {
        binding.notificationTag.text = "#${tag.tag}"
        binding.notificationCount.text = tag.notificationsCount.toString()
        val drawable = if (tag.visible) collapseDrawable else expandDrawable
        binding.collapseButtonImageView.setImageDrawable(drawable)
        binding.collapseButtonImageView.setOnClickListener { collapseListener(!tag.visible, tag.tag) }
        binding.root.setOnClickListener {
            navigatorApi.openTagActivity(tag.tag.removePrefix("#"))
        }
    }
}
