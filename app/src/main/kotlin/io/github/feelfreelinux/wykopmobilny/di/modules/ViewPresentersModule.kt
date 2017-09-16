package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.EntryVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.comment.EntryCommentVoteButtonPresenter

@Module
class ViewPresentersModule {
    @Provides
    fun providesEntryVoteButtonPresenter(entriesApi: EntriesApi) = EntryVoteButtonPresenter(entriesApi)

    @Provides
    fun providesEntryCommentVoteButtonPresenter(entriesApi: EntriesApi) = EntryCommentVoteButtonPresenter(entriesApi)
}