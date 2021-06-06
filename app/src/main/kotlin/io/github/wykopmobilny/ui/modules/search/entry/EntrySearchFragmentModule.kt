package io.github.wykopmobilny.ui.modules.search.entry

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.api.search.SearchApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.entries.EntriesInteractor

@Module
class EntrySearchFragmentModule {
    @Provides
    fun provideEntrySearchPresenter(
        schedulers: Schedulers,
        searchApi: SearchApi,
        entriesApi: EntriesApi,
        entriesInteractor: EntriesInteractor
    ) = EntrySearchPresenter(schedulers, searchApi, entriesApi, entriesInteractor)
}
