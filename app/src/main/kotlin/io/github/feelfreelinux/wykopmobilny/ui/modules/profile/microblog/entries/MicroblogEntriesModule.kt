package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.entries

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor

@Module
class MicroblogEntriesModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi, entriesApi: EntriesApi, entriesInteractor: EntriesInteractor) = MicroblogEntriesPresenter(schedulers, profileApi, entriesApi, entriesInteractor)
}