package io.github.wykopmobilny.ui.modules.profile.microblog.comments

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.entrycomments.EntryCommentInteractor

@Module
class MicroblogCommentsModule {

    @Provides
    fun providesPresenter(
        schedulers: Schedulers,
        profileApi: ProfileApi,
        entriesApi: EntriesApi,
        entryCommentsInteractor: EntryCommentInteractor
    ) = MicroblogCommentsPresenter(schedulers, profileApi, entriesApi, entryCommentsInteractor)
}
