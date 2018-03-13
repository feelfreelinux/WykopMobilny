package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext


class WykopImageView: ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val settingsPreferencesApi by lazy { SettingsPreferences(context) as SettingsPreferencesApi }

    var isResized = false

    init {
        setOnClickListener { openImageListener() }
        scaleType = ScaleType.CENTER_CROP
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

    var forceDisableMinimizedMode = false

    fun resetImage() {
        setImageBitmap(null)
    }

    val screenMetrics by lazy {
        val metrics = DisplayMetrics()
        getActivityContext()!!.windowManager.defaultDisplay.getMetrics(metrics)
        metrics
    }

    var openImageListener : () -> Unit = {}
    var onResizedListener : (Boolean) -> Unit = {}
    var showResizeView : (Boolean) -> Unit = {}


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val proportion = (screenMetrics.heightPixels.toFloat() * (settingsPreferencesApi.cutImageProportion.toFloat() / 100)) / screenMetrics.widthPixels.toFloat()
        val widthSpec = MeasureSpec.getSize(if (settingsPreferencesApi.showMinifiedImages && !forceDisableMinimizedMode) widthMeasureSpec/2 else widthMeasureSpec)
        if (drawable != null) {
            val measuredMultipler = (drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat())
            val heightSpec = widthSpec.toFloat() * measuredMultipler
            if (measuredMultipler > proportion && !isResized && settingsPreferencesApi.cutImages) {
                setOnClickListener {
                    isResized = true
                    onResizedListener(true)
                    requestLayout()
                    invalidate()
                    setOnClickListener({ openImageListener() })
                }
                setMeasuredDimension(widthSpec, (widthSpec.toFloat() * proportion).toInt())
                showResizeView(true)
            } else {
                setMeasuredDimension(widthSpec, heightSpec.toInt())
                setOnClickListener { openImageListener() }
                showResizeView(false)
            }
        } else {
            setMeasuredDimension(widthSpec, (widthSpec.toFloat() * proportion).toInt())
            setOnClickListener { openImageListener() }
            showResizeView(false)
        }
        if (!settingsPreferencesApi.cutImages) showResizeView(false)
    }
}