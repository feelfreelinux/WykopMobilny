package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import kotlinx.android.synthetic.main.wykopembedview.view.*
import java.lang.ref.WeakReference
import java.net.URI


class WykopEmbedView: FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    companion object {
        val NSFW_IMAGE_PLACEHOLDER = "https://www.wykop.pl/cdn/c2526412/nsfw.jpg"
    }

    var resized = false
    lateinit var mEmbed : WeakReference<Embed>
    lateinit var navigator : NewNavigatorApi
    var forceDisableMinimizedMode : Boolean
        get() = image.forceDisableMinimizedMode
        set(value) { image.forceDisableMinimizedMode = value }


    init {
        View.inflate(context, R.layout.wykopembedview, this)
        isVisible = false
        image.onResizedListener = {
            mEmbed.get()?.isResize = it
        }
        image.showResizeView = {
            post {
                imageExpand.isVisible = it
            }
        }
        image.openImageListener = { handleUrl() }
    }

    fun setEmbed(embed: Embed?, settingsPreferencesApi: SettingsPreferencesApi, navigatorApi: NewNavigatorApi) {
        resized = false
        navigator = navigatorApi
        if (embed == null) isVisible = false
        embed?.apply {
            image.resetImage()
            mEmbed = WeakReference(embed)
            image.isResized = embed.isResize
            imageExpand.isVisible = false
            isVisible = true
            if (plus18 && !settingsPreferencesApi.showAdultContent) image.loadImageFromUrl(NSFW_IMAGE_PLACEHOLDER)
            else {
                image.loadImageFromUrl(preview)
            }
            setEmbedIcon(embed)
        }
    }

    fun setEmbedIcon(embed : Embed) {
        imageIconYoutube.isVisible = false
        imageIconGfycat.isVisible = false
        imageIconVimeo.isVisible = false
        imageIconGifSize.isVisible = false
        val url = URI(embed.url)
        val domain = url.host.replace("www.", "").substringBeforeLast(".")
                .substringAfterLast(".")
        when(domain) {
            "youtube" -> {
                imageIconYoutube.isVisible = true
            }
            "youtu" -> {
                imageIconYoutube.isVisible = true
            }
            "gfycat" -> {
                imageIconGfycat.isVisible = true
            }
            "vimeo" -> {
                imageIconVimeo.isVisible = true
            }
            else -> {
                if (embed.isAnimated) {
                    imageIconGifSize.isVisible = true
                    val size = embed.size
                    if (size.length <= 6) {
                        imageIconGifSize.text = embed.size.toUpperCase()
                    } else {
                        imageIconGifSize.text = size.substringBefore(".") + size.substring(size.length - 2, size.length)
                    }
                }
            }
        }
    }

    fun handleUrl() {
        val image = mEmbed.get()!!
        when (image.type) {
            "image" -> {
                // APIV2 WTF
                val url = if (image.isAnimated) image.url.replace(".jpg", ".gif") else image.url
                navigator.openPhotoViewActivity(url)

            }
            "video" -> context.openBrowser(image.url) // @TODO replace with some nice implementation
        }
    }
}