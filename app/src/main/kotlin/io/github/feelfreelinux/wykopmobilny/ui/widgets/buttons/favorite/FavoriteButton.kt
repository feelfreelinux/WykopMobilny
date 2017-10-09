package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.ImageView
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.utils.isVisible
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

abstract class FavoriteButton : ImageView, FavoriteButtonView {
    constructor(context: Context) : super(context, null, R.attr.MirkoButtonStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.attr.MirkoButtonStyle)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var userManager : UserManagerApi

    init {
        WykopApp.uiInjector.inject(this)
        isVisible = userManager.isUserAuthorized()
    }

    override var isFavorite : Boolean
        get() = isSelected
        set(value) {
            if (value) {
                isSelected = true
                setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_favorite_selected)
                )
            } else {
                isSelected = false
                setImageDrawable(
                        ContextCompat.getDrawable(context, R.drawable.ic_favorite)
                )
            }
        }

    override fun showErrorDialog(e: Throwable) =
        context.showExceptionDialog(e)
}