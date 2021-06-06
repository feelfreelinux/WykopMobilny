package io.github.wykopmobilny.ui.widgets.buttons

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.use
import io.github.wykopmobilny.R

class FavoriteButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.MirkoButtonStyle
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var favoriteDrawable: Drawable
    private var favoriteOutlineDrawable: Drawable
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
        context.theme.resolveAttribute(R.attr.buttonStatelist, typedValue, true)
        setBackgroundResource(typedValue.resourceId)

        favoriteDrawable = context.obtainStyledAttributes(arrayOf(R.attr.favoriteDrawable).toIntArray())
            .use { it.getDrawable(0)!! }

        favoriteOutlineDrawable = context.obtainStyledAttributes(arrayOf(R.attr.favoriteOutlineDrawable).toIntArray())
            .use { it.getDrawable(0)!! }
    }
}
