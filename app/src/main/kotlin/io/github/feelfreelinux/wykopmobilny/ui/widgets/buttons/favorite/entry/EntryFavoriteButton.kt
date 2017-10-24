package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite.entry

import android.content.Context
import android.util.AttributeSet
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite.FavoriteButton
import javax.inject.Inject

class EntryFavoriteButton : FavoriteButton, EntryFavoriteButtonView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override var entryId: Int = 0
    @Inject lateinit var presenter : EntryFavoriteButtonPresenter

    init {
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        setOnClickListener {
            presenter.markFavorite()
        }
    }

    fun setEntryData(entry : Entry) {
        entryId = entry.id
        isFavorite = entry.isFavorite
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.unsubscribe()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.subscribe(this)
    }
}