package io.github.wykopmobilny.ui.modules.favorite.entry

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.entries.EntriesInteractor

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
