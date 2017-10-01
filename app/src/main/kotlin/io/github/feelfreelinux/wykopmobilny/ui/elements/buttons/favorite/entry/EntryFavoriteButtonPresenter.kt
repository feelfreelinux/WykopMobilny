package io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.favorite.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.models.pojo.entries.FavoriteEntryResponse
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.reactivex.functions.Consumer

class EntryFavoriteButtonPresenter(private val subscriptionHelperApi: SubscriptionHelperApi, private val entriesApi: EntriesApi) : BasePresenter<EntryFavoriteButtonView>() {
    fun markFavorite() {
        subscriptionHelperApi.subscribe(
                entriesApi.markFavorite(view?.entryId!!),
                { view?.isFavorite = it.userFavorite },
                { view?.showErrorDialog(it) }, this
        )

    }
}