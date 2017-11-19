package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.*
import kotlinx.android.synthetic.main.wykopimageview_layout.view.*
import javax.inject.Inject

class WykopImageView : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        val NSFW_IMAGE_PLACEHOLDER = "https://www.wykop.pl/cdn/c2526412/nsfw.jpg"
    }

    @Inject lateinit var settingsPreferences: SettingsPreferencesApi
    @Inject lateinit var navigatorApi: NavigatorApi

    init {
        WykopApp.uiInjector.inject(this)
        isVisible = false
    }

    fun setEmbed(embed: Embed?) {
        if (embed == null) isVisible = false
        embed?.apply {
            isVisible = true
            if (plus18 && !settingsPreferences.showAdultContent) loadImageFromUrl(NSFW_IMAGE_PLACEHOLDER)
            else loadImageFromUrl(preview)
            setOnClickListener {
                when (type) {
                    "image" -> navigatorApi.openPhotoViewActivity(context as Activity, url)
                    "video" -> context.openBrowser(url) // @TODO replace with some nice implementation
                }
            }
        }
    }

    fun loadImageFromUrl(url : String) {
        GlideApp
                .with(context)
                .load(url)
                .listener(KotlinGlideRequestListener(
                        { progressView.isVisible = false },
                        { progressView.isVisible = false }))
                .into(image)
    }
}