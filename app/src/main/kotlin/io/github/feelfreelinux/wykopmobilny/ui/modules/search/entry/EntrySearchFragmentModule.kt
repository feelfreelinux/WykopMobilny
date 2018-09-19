package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor

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