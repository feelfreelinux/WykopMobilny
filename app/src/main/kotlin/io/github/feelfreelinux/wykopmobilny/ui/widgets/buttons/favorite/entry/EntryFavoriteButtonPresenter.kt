package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntryFavoriteButtonPresenter(private val subscriptionHelperApi: SubscriptionHelperApi, private val entriesApi: EntriesApi) : BasePresenter<EntryFavoriteButtonView>() {
    var entryId : Int = -1

    fun markFavorite() {
        subscriptionHelperApi.subscribe(
                entriesApi.markFavorite(entryId),
                { view?.isFavorite = it.userFavorite },
                { view?.showErrorDialog(it) }, this
        )

    }
}