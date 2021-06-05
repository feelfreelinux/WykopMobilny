package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.comments

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentInteractor

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
