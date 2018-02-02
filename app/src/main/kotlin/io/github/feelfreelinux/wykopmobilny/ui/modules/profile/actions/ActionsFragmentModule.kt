package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class ActionsFragmentModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) = ActionsFragmentPresenter(schedulers, profileApi)
}