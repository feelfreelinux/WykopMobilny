package io.github.feelfreelinux.wykopmobilny.ui.elements.wykop_image_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.github.chrisbanes.photoview.PhotoView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.ui.photoview.launchPhotoView
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser

class WykopImageView : ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        isVisible = false
    }

    fun setEmbed(embed : Embed?) {
        if (embed == null) isVisible = false
        embed?.apply {
            isVisible = true
            loadImage(preview)
            // @TODO We should put +18 check here and handle video embed type
            setOnClickListener {
                when (type) {
                    "image" -> context.launchPhotoView(url)
                    "video" -> context.openBrowser(url) // @TODO replace with some nice implementation
                }
            }
        }
    }
}