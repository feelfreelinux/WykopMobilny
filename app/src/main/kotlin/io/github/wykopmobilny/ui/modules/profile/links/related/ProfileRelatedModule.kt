package io.github.wykopmobilny.ui.modules.profile.links.related

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.Schedulers

@Module
class ProfileRelatedModule {

    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) =
        ProfileRelatedPresenter(schedulers, profileApi)
}
