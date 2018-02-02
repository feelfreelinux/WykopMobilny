package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged

import dagger.Module
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class DiggedLinksModule {
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) = DiggedLinksPresenter(schedulers, profileApi)
}