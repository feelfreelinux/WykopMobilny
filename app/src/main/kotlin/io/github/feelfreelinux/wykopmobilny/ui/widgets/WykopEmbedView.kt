package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import android.widget.FrameLayout
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import kotlinx.android.synthetic.main.wykopembedview.view.*
import java.lang.ref.WeakReference
import java.net.URI

class WykopEmbedView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        const val NSFW_IMAGE_PLACEHOLDER = "https://www.wykop.pl/cdn/c2526412/nsfw.jpg"
    }

    init {
        View.inflate(context, R.layout.wykopembedview, this)
        isVisible = false
        image.onResizedListener = { mEmbed.get()?.isResize = it }
        image.showResizeView = { post { imageExpand.isVisible = it } }
        image.openImageListener = { handleUrl() }
    }

    var resized = false
    var hiddenPreview: String? = null
    lateinit var mEmbed: WeakReference<Embed>
    lateinit var navigator: NewNavigatorApi
    lateinit var settingsPreferences: SettingsPreferencesApi
    var forceDisableMinimizedMode: Boolean
        get() = image.forceDisableMinimizedMode
        set(value) {
            image.forceDisableMinimizedMode = value
        }

    fun setEmbed(embed: Embed?, settingsPreferencesApi: SettingsPreferencesApi, navigatorApi: NewNavigatorApi, isNsfw: Boolean = false) {
        hiddenPreview = null
        image.isVisible = true
        resized = false
        settingsPreferences = settingsPreferencesApi
        navigator = navigatorApi
        if (embed == null || !Patterns.WEB_URL.matcher(embed.url.replace("\\", "")).matches()) isVisible = false
        else {
            embed.apply {
                image.resetImage()
                mEmbed = WeakReference(embed)
                image.isResized = embed.isResize
                imageExpand.isVisible = false
                isVisible = true
                if (!embed.isRevealed && (plus18 && !settingsPreferencesApi.showAdultContent || isNsfw && settingsPreferencesApi.hideNsfw)) {
                    image.loadImageFromUrl(NSFW_IMAGE_PLACEHOLDER)
                    hiddenPreview = preview
                } else {
                    image.loadImageFromUrl(preview)
                }
                setEmbedIcon(embed)
            }
        }
    }

    private fun setEmbedIcon(embed: Embed) {
        imageIconGifSize.isVisible = false
        imageIcon.isVisible = true
        val url = URI(embed.url.replace("\\", ""))
        val domain = url.host.replace("www.", "").substringBeforeLast(".")
            .substringAfterLast(".")
        when (domain) {
            "youtube" -> imageIcon.setBackgroundResource(R.mipmap.ic_youtube)
            "youtu" -> imageIcon.setBackgroundResource(R.mipmap.ic_youtube)
            "gfycat" -> imageIcon.setBackgroundResource(R.mipmap.ic_gfycat)
            "vimeo" -> imageIcon.setBackgroundResource(R.mipmap.ic_vimeo)
            "streamable" -> imageIcon.setBackgroundResource(R.mipmap.ic_streamable)
            "coub" -> imageIcon.setBackgroundResource(R.mipmap.ic_coub)
            "twitch" -> imageIcon.setBackgroundResource(R.mipmap.ic_twitch)
            "twitter" -> imageIcon.setBackgroundResource(R.mipmap.ic_twitter)
            "instagram" -> imageIcon.setBackgroundResource(R.mipmap.ic_instagram)
            "facebook" -> imageIcon.setBackgroundResource(R.mipmap.ic_facebook)
            "soundcloud" -> imageIcon.setBackgroundResource(R.mipmap.ic_soundcloud)
            "hearthis" -> imageIcon.setBackgroundResource(R.mipmap.ic_hearthis)
            "mixcould" -> imageIcon.setBackgroundResource(R.mipmap.ic_mixcloud)
            else -> {
                if (embed.isAnimated) {
                    imageIconGifSize.isVisible = true
                    imageIcon.isVisible = false
                    val size = embed.size
                    if (size.length <= 6) {
                        imageIconGifSize.text = embed.size.toUpperCase()
                    } else {
                        imageIconGifSize.text = size.substringBefore(".") + size.substring(size.length - 2, size.length)
                    }
                } else imageIcon.isVisible = false
            }
        }
    }

    fun handleUrl() {
        if (hiddenPreview != null) {
            image.loadImageFromUrl(hiddenPreview.toString())
            hiddenPreview = null
            mEmbed.get()?.isRevealed = true
            return
        }
        val image = mEmbed.get()!!
        when (image.type) {
            "image" -> {
                // APIV2 WTF
                val url = if (image.isAnimated) image.url.replace(".jpg", ".gif") else image.url
                navigator.openPhotoViewActivity(url)

            }
            "video" -> {
                val url = URI(image.url.replace("\\", ""))
                val domain = url.host.replace("www.", "").substringBeforeLast(".")
                    .substringAfterLast(".")
                when (domain) {
                    "youtube", "youtu" -> {
                        if (settingsPreferences.enableYoutubePlayer) {
                            navigator.openYoutubeActivity(image.url)
                        } else {
                            getActivityContext()!!.openBrowser(image.url)
                        }
                    }
                    "gfycat", "streamable", "coub" -> {
                        if (settingsPreferences.enableEmbedPlayer) {
                            navigator.openEmbedActivity(image.url)
                        } else {
                            getActivityContext()!!.openBrowser(image.url)
                        }
                    }
                    else -> navigator.openBrowser(image.url)
                }
            }
        }
    }
}