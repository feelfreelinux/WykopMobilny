package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class EntryDetailModule {
    @Provides
    fun providesEntryDetailPresenter(schedulers: Schedulers, entriesApi: EntriesApi) =
            EntryDetailPresenter(schedulers, entriesApi)
}