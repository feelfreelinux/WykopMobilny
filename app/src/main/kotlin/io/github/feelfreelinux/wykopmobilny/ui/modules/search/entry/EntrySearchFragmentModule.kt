package io.github.feelfreelinux.wykopmobilny.ui.modules.search.entry

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class EntrySearchFragmentModule {
    @Provides
    fun provideEntrySearchPresenter(schedulers: Schedulers, searchApi: SearchApi) = EntrySearchPresenter(schedulers, searchApi)
}