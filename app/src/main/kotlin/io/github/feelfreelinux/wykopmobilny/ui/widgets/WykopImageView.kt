package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import com.github.chrisbanes.photoview.PhotoView
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferences
import io.github.feelfreelinux.wykopmobilny.utils.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext


class WykopImageView: PhotoView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val settingsPreferencesApi by lazy { SettingsPreferences(context) as SettingsPreferencesApi }

    var isResized = false

    init {
        setOnClickListener { clickImageListener() }
        setOnLongClickListener { longPressListener() }
        scaleType = ScaleType.CENTER_CROP
    }

    fun loadImageFromUrl(url : String) {
        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .into(this)
    }

    fun loadGifFromUrl(url : String) {
        GlideApp.with(context).asGif().load(url.replace(".jpg", ".gif"))
                .into(this)
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

    var clickImageListener: () -> Unit = {}
    var longPressListener: () -> Boolean = { false }
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
                    setOnClickListener({ clickImageListener() })
                }
                setMeasuredDimension(widthSpec, (widthSpec.toFloat() * proportion).toInt())
                showResizeView(true)
            } else {
                setMeasuredDimension(widthSpec, heightSpec.toInt())
                setOnClickListener { clickImageListener() }
                showResizeView(false)
            }
        } else {
            setMeasuredDimension(widthSpec, (widthSpec.toFloat() * proportion).toInt())
            setOnClickListener { clickImageListener() }
            showResizeView(false)
        }
        if (!settingsPreferencesApi.cutImages) showResizeView(false)
    }
}