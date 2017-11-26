package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.*
import javax.inject.Inject

class WykopImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
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
            if (plus18 && !settingsPreferences.showAdultContent) loadImage(NSFW_IMAGE_PLACEHOLDER)
            else loadImage(preview)
            setOnClickListener {
                when (type) {
                    "image" -> navigatorApi.openPhotoViewActivity(context as Activity, url)
                    "video" -> context.openBrowser(url) // @TODO replace with some nice implementation
                }
            }
        }
    }

    /*override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (heightMeasureSpec > maxHeight || heightMeasureSpec == 0) {
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(maxHeight,
                    MeasureSpec.getMode(widthMeasureSpec)))
        }
    }*/

}