package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.entries

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor

@Module
class TagEntriesModule {
    @Provides
    fun provideTagEntriesPresenter(schedulers: Schedulers, tagApi: TagApi, entriesApi: EntriesApi, entriesInteractor: EntriesInteractor) =
            TagEntriesPresenter(schedulers, tagApi, entriesApi, entriesInteractor)
}