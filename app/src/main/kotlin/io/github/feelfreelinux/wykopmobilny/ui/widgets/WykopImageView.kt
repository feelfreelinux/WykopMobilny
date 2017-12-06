package io.github.feelfreelinux.wykopmobilny.ui.widgets

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import io.github.feelfreelinux.wykopmobilny.glide.GlideApp
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import io.github.feelfreelinux.wykopmobilny.utils.printout


class WykopImageView: ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    companion object {
        val WIDTH_HEIGHT_MULTIPLER = 1.13f
    }

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

    fun resetImage() {
        setImageBitmap(null)
    }

    var openImageListener : () -> Unit = {}
    var onResizedListener : (Boolean) -> Unit = {}
    var showResizeView : (Boolean) -> Unit = {}


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpec = MeasureSpec.getSize(widthMeasureSpec)
        if (drawable != null) {
            val measuredMultipler = (drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat())
            val heightSpec = widthSpec.toFloat() * measuredMultipler
            if (measuredMultipler > WIDTH_HEIGHT_MULTIPLER && !isResized) {
                setOnClickListener {
                    isResized = true
                    onResizedListener(true)
                    requestLayout()
                    invalidate()
                    setOnClickListener({ openImageListener() })
                }
                setMeasuredDimension(widthSpec, (widthSpec.toFloat() * WIDTH_HEIGHT_MULTIPLER).toInt())
                showResizeView(true)
            } else {
                setMeasuredDimension(widthSpec, heightSpec.toInt())
                setOnClickListener { openImageListener() }
                showResizeView(false)
            }
        } else {
            setMeasuredDimension(widthSpec, (widthSpec.toFloat() * WIDTH_HEIGHT_MULTIPLER).toInt())
            setOnClickListener { openImageListener() }
            showResizeView(false)
        }
    }
}