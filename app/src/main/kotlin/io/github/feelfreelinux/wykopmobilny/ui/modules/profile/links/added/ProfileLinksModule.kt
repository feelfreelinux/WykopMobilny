package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class ProfileLinksModule {
    @Provides
    fun provideAddedLinksPresenter(
        schedulers: Schedulers,
        profileApi: ProfileApi,
        linksInteractor: LinksInteractor
    ) = ProfileLinksPresenter(schedulers, profileApi, linksInteractor)
}