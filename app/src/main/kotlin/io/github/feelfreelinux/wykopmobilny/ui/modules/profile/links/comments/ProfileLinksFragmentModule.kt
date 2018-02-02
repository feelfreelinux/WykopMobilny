package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class ProfileLinksFragmentModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) = ProfileLinksFragmentPresenter(schedulers, profileApi)
}