package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext

class FavoriteButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.MirkoButtonStyle
) : ImageView(context, attrs, defStyleAttr) {

    var favoriteDrawable: Drawable
    var favoriteOutlineDrawable: Drawable
    var typedArray: TypedArray
    var isFavorite: Boolean
        get() = isSelected
        set(value) {
            if (value) {
                isSelected = true
                setImageDrawable(favoriteDrawable)
            } else {
                isSelected = false
                setImageDrawable(favoriteOutlineDrawable)
            }
        }

    init {
        val typedValue = TypedValue()
        getActivityContext()!!.theme?.resolveAttribute(R.attr.buttonStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)

        typedArray = context.obtainStyledAttributes(
            arrayOf(
                R.attr.favoriteDrawable
            ).toIntArray()
        )
        favoriteDrawable = typedArray.getDrawable(0)!!
        typedArray.recycle()

        typedArray = context.obtainStyledAttributes(arrayOf(R.attr.favoriteOutlineDrawable).toIntArray())
        favoriteOutlineDrawable = typedArray.getDrawable(0)!!
        typedArray.recycle()
    }
}