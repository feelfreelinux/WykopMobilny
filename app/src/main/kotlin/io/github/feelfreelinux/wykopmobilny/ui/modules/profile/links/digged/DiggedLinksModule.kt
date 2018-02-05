package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class DiggedLinksModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) = DiggedLinksPresenter(schedulers, profileApi)
}