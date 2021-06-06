package io.github.wykopmobilny.ui.fragments.entries

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.base.Schedulers

@Module
class EntriesFragmentModule {

    @Provides
    fun provideEntriesFragmentPresenter(
        schedulers: Schedulers,
        entriesApi: EntriesApi,
        entryInteractor: EntriesInteractor
    ) = EntriesFragmentPresenter(schedulers, entriesApi, entryInteractor)
}
