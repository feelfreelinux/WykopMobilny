package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class EditEntryCommentActivityModule {
    @Provides
    fun provideEditEntryCommentPresenter(schedulers: Schedulers, entriesApi: EntriesApi) =
        EditEntryCommentPresenter(schedulers, entriesApi)
}