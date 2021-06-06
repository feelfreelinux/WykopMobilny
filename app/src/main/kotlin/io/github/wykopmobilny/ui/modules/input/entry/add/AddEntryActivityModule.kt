package io.github.wykopmobilny.ui.modules.input.entry.add

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.base.Schedulers

@Module
class AddEntryActivityModule {
    @Provides
    fun provideAddEntryPresenter(schedulers: Schedulers, entriesApi: EntriesApi) = AddEntryPresenter(schedulers, entriesApi)
}
