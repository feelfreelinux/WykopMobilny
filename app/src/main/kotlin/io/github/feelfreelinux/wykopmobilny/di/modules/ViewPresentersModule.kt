package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.EntryVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.comment.EntryCommentVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

@Module
class ViewPresentersModule {
    @Provides
    fun providesEntryVoteButtonPresenter(subscriptionHelperApi: SubscriptionHelperApi, entriesApi: EntriesApi) = EntryVoteButtonPresenter(subscriptionHelperApi, entriesApi)

    @Provides
    fun providesEntryCommentVoteButtonPresenter(subscriptionHelperApi: SubscriptionHelperApi, entriesApi: EntriesApi) = EntryCommentVoteButtonPresenter(subscriptionHelperApi, entriesApi)
}