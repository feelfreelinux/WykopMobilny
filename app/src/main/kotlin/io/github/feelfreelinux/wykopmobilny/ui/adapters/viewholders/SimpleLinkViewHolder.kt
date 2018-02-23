package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v4.content.ContextCompat
import android.view.View
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.simple_link_layout.*

class SimpleLinkViewHolder(override val containerView: View, settingsApi: SettingsPreferencesApi) : BaseLinkViewHolder(containerView, settingsApi) {
    val diggCountDrawable by lazy {
        val typedArray = containerView.context.obtainStyledAttributes(arrayOf(
                R.attr.digCountDrawable).toIntArray())
        val selectedDrawable = typedArray.getDrawable(0)
        typedArray.recycle()
        selectedDrawable
    }

    override fun bindView(link: Link) {
        super.bindView(link)

        simple_digg.background = when (link.userVote) {
            "dig" -> ContextCompat.getDrawable(containerView.context, R.drawable.ic_dig_vote)
            "bury" -> ContextCompat.getDrawable(containerView.context, R.drawable.ic_dig_bury)
            else -> diggCountDrawable
        }
        simple_digg_count.text = link.voteCount.toString()
        simple_title.text = link.title
        hotBadgeStripSimple.isVisible = link.isHot
        simple_digg_hot.isVisible = link.isHot
        if (settingsApi.linkShowImage) {
            simple_image.isVisible = link.preview != null
            link.preview?.let { simple_image.loadImage(link.preview) }
        }

        containerView.setOnClickListener {
            openLinkDetail()
        }
    }

    override fun setWidgetAlpha(alpha: Float) {
        simple_image.alpha = alpha
        simple_title.alpha = alpha
    }

    override fun cleanRecycled() {
        containerView.apply {
            GlideApp.with(containerView).clear(simple_image)
            simple_title.text = null
            containerView.setOnClickListener(null)
        }
    }
}