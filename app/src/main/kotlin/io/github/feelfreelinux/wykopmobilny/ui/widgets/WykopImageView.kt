package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
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


class WykopImageView: ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    companion object {
        val NSFW_IMAGE_PLACEHOLDER = "https://www.wykop.pl/cdn/c2526412/nsfw.jpg"
        val WIDTH_HEIGHT_MULTIPLER = 1.13f
    }

    var resized = false
    lateinit var mEmbed : Embed

    @Inject lateinit var settingsPreferences: SettingsPreferencesApi
    @Inject lateinit var navigatorApi: NavigatorApi



    init {
        scaleType = ScaleType.CENTER_CROP
        WykopApp.uiInjector.inject(this)
        isVisible = false
    }

    fun setEmbed(embed: Embed?) {
        resized = false
        if (embed == null) isVisible = false
        embed?.apply {
            mEmbed = embed
            isVisible = true
            if (plus18 && !settingsPreferences.showAdultContent) loadImageFromUrl(NSFW_IMAGE_PLACEHOLDER)
            else loadImageFromUrl(preview)
            setOnClickListener {
                handleUrl()
            }
        }
    }

    fun handleUrl() {
        when (mEmbed.type) {
            "image" -> navigatorApi.openPhotoViewActivity(context as Activity, mEmbed.url)
            "video" -> context.openBrowser(mEmbed.url) // @TODO replace with some nice implementation
        }
    }

    fun loadImageFromUrl(url : String) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        setImageBitmap(resource)
                    }
                })
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        if (drawable != null) {
            val measuredMultipler = (drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat())
            val heightSpec = widthSpec.toFloat() * measuredMultipler
            if (measuredMultipler > WIDTH_HEIGHT_MULTIPLER && !resized) {
                setMeasuredDimension(widthSpec, (widthSpec.toFloat() * WIDTH_HEIGHT_MULTIPLER).toInt())
                setOnClickListener {
                    resized = true
                    requestLayout()
                    invalidate()
                    setOnClickListener {
                        handleUrl()
                    }

                }
            } else setMeasuredDimension(widthSpec, heightSpec.toInt())
        } else setMeasuredDimension(widthSpec, (widthSpec.toFloat() * WIDTH_HEIGHT_MULTIPLER).toInt())
    }
}