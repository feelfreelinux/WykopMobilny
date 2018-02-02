package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class AddedLinksModule {
    @Provides
    fun provideAddedLinksPresenter(schedulers: Schedulers, profileApi: ProfileApi) = AddedLinksPresenter(schedulers, profileApi)
}