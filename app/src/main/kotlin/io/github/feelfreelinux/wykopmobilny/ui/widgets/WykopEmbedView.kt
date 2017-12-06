package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.*
import javax.inject.Inject
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
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

    @Inject lateinit var settingsPreferences: SettingsPreferencesApi
    @Inject lateinit var navigatorApi: NavigatorApi



    init {
        View.inflate(context, R.layout.wykopembedview, this)
        WykopApp.uiInjector.inject(this)
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

    fun setEmbed(embed: Embed?) {
        resized = false
        if (embed == null) isVisible = false
        embed?.apply {
            image.resetImage()
            mEmbed = WeakReference(embed)
            image.isResized = embed.isResize
            imageExpand.isVisible = false
            isVisible = true
            if (plus18 && !settingsPreferences.showAdultContent) image.loadImageFromUrl(NSFW_IMAGE_PLACEHOLDER)
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
                navigatorApi.openPhotoViewActivity(context as Activity, url)

            }
            "video" -> context.openBrowser(image.url) // @TODO replace with some nice implementation
        }
    }
}