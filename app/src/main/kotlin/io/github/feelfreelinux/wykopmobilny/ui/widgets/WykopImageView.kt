package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.ui.modules.photoview.launchPhotoView
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.loadImage
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import javax.inject.Inject

class WykopImageView : ImageView {
    companion object {
        val NSFW_IMAGE_PLACEHOLDER = "https://www.wykop.pl/cdn/c2526412/nsfw.jpg"
    }

    @Inject lateinit var settingsPreferences : SettingsPreferencesApi

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        WykopApp.uiInjector.inject(this)
        isVisible = false
    }

    fun setEmbed(embed : Embed?) {
        if (embed == null) isVisible = false
        embed?.apply {
            isVisible = true
            if (plus18 && !settingsPreferences.showAdultContent) loadImage(NSFW_IMAGE_PLACEHOLDER)
            else loadImage(preview)
            setOnClickListener {
                when (type) {
                    "image" -> context.launchPhotoView(url)
                    "video" -> context.openBrowser(url) // @TODO replace with some nice implementation
                }
            }
        }
    }
}