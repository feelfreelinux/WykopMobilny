package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.burried

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class BurriedLinksModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) = BurriedLinksPresenter(schedulers, profileApi)
}