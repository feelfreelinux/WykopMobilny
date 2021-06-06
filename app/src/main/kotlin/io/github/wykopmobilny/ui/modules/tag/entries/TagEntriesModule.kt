package io.github.wykopmobilny.ui.modules.tag.entries

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.api.tag.TagApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.entries.EntriesInteractor

@Module
class TagEntriesModule {

    @Provides
    fun provideTagEntriesPresenter(
        schedulers: Schedulers,
        tagApi: TagApi,
        entriesApi: EntriesApi,
        entriesInteractor: EntriesInteractor
    ) = TagEntriesPresenter(schedulers, tagApi, entriesApi, entriesInteractor)
}
