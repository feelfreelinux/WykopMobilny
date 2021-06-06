package io.github.wykopmobilny.ui.modules.input.entry.edit

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.base.Schedulers

@Module
class EditEntryActivityModule {
    @Provides
    fun provideEditEntryPresenter(schedulers: Schedulers, entriesApi: EntriesApi) = EditEntryPresenter(schedulers, entriesApi)
}
