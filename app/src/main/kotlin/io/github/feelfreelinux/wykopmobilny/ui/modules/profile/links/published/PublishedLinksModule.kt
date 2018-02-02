package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.published

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class PublishedLinksModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) = PublishedLinksPresenter(schedulers, profileApi)
}