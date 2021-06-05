package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor

@Module
class EntryFavoriteFragmentModule {

    @Provides
    fun provideLinksFavoritePresenter(
        schedulers: Schedulers,
        entriesApi: EntriesApi,
        entriesInteractor: EntriesInteractor
    ) =
        EntryFavoritePresenter(
            schedulers,
            entriesInteractor,
            entriesApi,
        )
}
