package io.github.wykopmobilny.ui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import io.github.wykopmobilny.WykopApp
import io.github.wykopmobilny.glide.GlideApp
import io.github.wykopmobilny.utils.getActivityContext

class WykopImageView(context: Context, attrs: AttributeSet?,) : AppCompatImageView(context, attrs) {

    init {
        setOnClickListener { openImageListener() }
        scaleType = ScaleType.CENTER_CROP
    }

    var isResized = false
    var forceDisableMinimizedMode = false
    var openImageListener: () -> Unit = {}
    var onResizedListener: (Boolean) -> Unit = {}
    var showResizeView: (Boolean) -> Unit = {}

    private val settingsPreferencesApi by lazy { (context.applicationContext as WykopApp).settingsPreferencesApi.get() }
    private val screenMetrics by lazy {
        val metrics = DisplayMetrics()
        getActivityContext()!!.windowManager.defaultDisplay.getMetrics(metrics)
        metrics
    }

    fun loadImageFromUrl(url: String) {
        GlideApp.with(context)
            .asBitmap()
            .load(url)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .signature(ObjectKey(url))
            )
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    setImageBitmap(resource)
                }
            })
    }

    fun resetImage() {
        setImageBitmap(null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val targetHeightPercentage = settingsPreferencesApi.cutImageProportion.toFloat() / 100
        val proportion = (screenMetrics.heightPixels.toFloat() * targetHeightPercentage) / screenMetrics.widthPixels.toFloat()
        val widthSpec = MeasureSpec.getSize(
            if (settingsPreferencesApi.showMinifiedImages && !forceDisableMinimizedMode) widthMeasureSpec / 2 else widthMeasureSpec
        )
        if (drawable != null) {
            val measuredMultiplier = (drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat())
            val heightSpec = widthSpec.toFloat() * measuredMultiplier
            if (measuredMultiplier > proportion && !isResized && settingsPreferencesApi.cutImages) {
                setOnClickListener {
                    isResized = true
                    onResizedListener(true)
                    requestLayout()
                    invalidate()
                    setOnClickListener { openImageListener() }
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
