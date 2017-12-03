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

        image.openImageListener = { handleUrl() }
    }

    fun setEmbed(embed: Embed?) {
        resized = false
        if (embed == null) isVisible = false
        embed?.apply {
            image.resetImage()
            mEmbed = WeakReference(embed)
            image.isResized = embed.isResize
            isVisible = true
            if (plus18 && !settingsPreferences.showAdultContent) image.loadImageFromUrl(NSFW_IMAGE_PLACEHOLDER)
            else image.loadImageFromUrl(preview)
        }
    }

    fun handleUrl() {
        val image = mEmbed.get()!!
        when (image.type) {
            "image" -> navigatorApi.openPhotoViewActivity(context as Activity, image.url)
            "video" -> context.openBrowser(image.url) // @TODO replace with some nice implementation
        }
    }
}