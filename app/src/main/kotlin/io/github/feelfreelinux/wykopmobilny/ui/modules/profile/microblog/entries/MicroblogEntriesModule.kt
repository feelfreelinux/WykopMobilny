package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.entries

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class MicroblogEntriesModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) = MicroblogEntriesPresenter(schedulers, profileApi)
}