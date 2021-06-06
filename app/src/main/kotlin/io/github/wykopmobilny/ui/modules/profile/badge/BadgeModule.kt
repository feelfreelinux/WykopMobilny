package io.github.wykopmobilny.ui.modules.profile.badge

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.Schedulers

@Module
class BadgeModule {
    @Provides
    fun provideBadgePresenter(schedulers: Schedulers, profileApi: ProfileApi) =
        BadgePresenter(schedulers, profileApi)
}
