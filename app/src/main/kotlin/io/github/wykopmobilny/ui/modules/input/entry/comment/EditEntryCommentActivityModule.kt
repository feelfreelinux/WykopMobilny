package io.github.wykopmobilny.ui.modules.input.entry.comment

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.base.Schedulers

@Module
class EditEntryCommentActivityModule {
    @Provides
    fun provideEditEntryCommentPresenter(schedulers: Schedulers, entriesApi: EntriesApi) =
        EditEntryCommentPresenter(schedulers, entriesApi)
}
