package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.badge

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class BadgeModule {
    @Provides
    fun provideBadgePresenter(schedulers: Schedulers, profileApi: ProfileApi) =
        BadgePresenter(schedulers, profileApi)
}