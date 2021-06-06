package io.github.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import android.widget.FrameLayout
import androidx.core.view.isVisible
import io.github.wykopmobilny.R
import io.github.wykopmobilny.databinding.WykopembedviewBinding
import io.github.wykopmobilny.models.dataclass.Embed
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.getActivityContext
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.openBrowser
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import java.lang.ref.WeakReference
import java.net.URI

class WykopEmbedView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    companion object {
        const val NSFW_IMAGE_PLACEHOLDER = "https://www.wykop.pl/cdn/c2526412/nsfw.jpg"
    }

    private val binding = WykopembedviewBinding.inflate(layoutInflater, this)

    init {
        this.isVisible = false
        binding.image.onResizedListener = { mEmbed.get()?.isResize = it }
        binding.image.showResizeView = { post { binding.imageExpand.isVisible = it } }
        binding.image.openImageListener = { handleUrl() }
    }

    var resized = false
    private var hiddenPreview: String? = null
    lateinit var mEmbed: WeakReference<Embed>
    lateinit var navigator: NewNavigatorApi
    lateinit var settingsPreferences: SettingsPreferencesApi
    var forceDisableMinimizedMode: Boolean
        get() = binding.image.forceDisableMinimizedMode
        set(value) {
            binding.image.forceDisableMinimizedMode = value
        }

    fun setEmbed(embed: Embed?, settingsPreferencesApi: SettingsPreferencesApi, navigatorApi: NewNavigatorApi, isNsfw: Boolean = false) {
        hiddenPreview = null
        binding.image.isVisible = true
        resized = false
        settingsPreferences = settingsPreferencesApi
        navigator = navigatorApi
        if (embed == null || !Patterns.WEB_URL.matcher(embed.url.replace("\\", "")).matches()) isVisible = false
        else {
            embed.apply {
                binding.image.resetImage()
                mEmbed = WeakReference(embed)
                binding.image.isResized = embed.isResize
                binding.imageExpand.isVisible = false
                isVisible = true
                if (
                    !embed.isRevealed &&
                    (plus18 && !settingsPreferencesApi.showAdultContent || isNsfw && settingsPreferencesApi.hideNsfw)
                ) {
                    binding.image.loadImageFromUrl(NSFW_IMAGE_PLACEHOLDER)
                    hiddenPreview = preview
                } else {
                    binding.image.loadImageFromUrl(preview)
                }
                setEmbedIcon(embed)
            }
        }
    }

    private fun setEmbedIcon(embed: Embed) {
        binding.imageIconGifSize.isVisible = false
        binding.imageIcon.isVisible = true
        val url = URI(embed.url.replace("\\", ""))
        val domain = url.host.replace("www.", "").substringBeforeLast(".")
            .substringAfterLast(".")
        when (domain) {
            "youtube" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_youtube)
            "youtu" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_youtube)
            "gfycat" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_gfycat)
            "vimeo" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_vimeo)
            "streamable" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_streamable)
            "coub" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_coub)
            "twitch" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_twitch)
            "twitter" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_twitter)
            "instagram" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_instagram)
            "facebook" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_facebook)
            "soundcloud" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_soundcloud)
            "hearthis" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_hearthis)
            "mixcould" -> binding.imageIcon.setBackgroundResource(R.mipmap.ic_mixcloud)
            else -> {
                if (embed.isAnimated) {
                    binding.imageIconGifSize.isVisible = true
                    binding.imageIcon.isVisible = false
                    val size = embed.size
                    if (size.length <= 6) {
                        binding.imageIconGifSize.text = embed.size.uppercase()
                    } else {
                        binding.imageIconGifSize.text = size.substringBefore(".") + size.substring(size.length - 2, size.length)
                    }
                } else binding.imageIcon.isVisible = false
            }
        }
    }

    fun handleUrl() {
        if (hiddenPreview != null) {
            binding.image.loadImageFromUrl(hiddenPreview.toString())
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
                    else -> navigator.openBrowser(settingsPreferences, image.url)
                }
            }
        }
    }
}
