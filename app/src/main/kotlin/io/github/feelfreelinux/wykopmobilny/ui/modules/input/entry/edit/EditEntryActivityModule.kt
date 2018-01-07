package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class EditEntryActivityModule {
    @Provides
    fun provideEditEntryPresenter(schedulers: Schedulers, entriesApi: EntriesApi) = EditEntryPresenter(schedulers, entriesApi)
}