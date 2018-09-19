package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class ProfileRelatedModule {

    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi) =
        ProfileRelatedPresenter(schedulers, profileApi)
}