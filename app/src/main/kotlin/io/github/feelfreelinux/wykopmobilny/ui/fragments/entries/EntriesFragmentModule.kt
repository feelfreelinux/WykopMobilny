package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class EntriesFragmentModule {
    @Provides
    fun provideEntriesFragmentPresenter(schedulers: Schedulers, entriesApi: EntriesApi) = EntriesFragmentPresenter(schedulers, entriesApi)
}