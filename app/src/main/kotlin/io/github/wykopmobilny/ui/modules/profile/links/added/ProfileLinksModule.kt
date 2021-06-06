package io.github.wykopmobilny.ui.modules.profile.links.added

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class ProfileLinksModule {
    @Provides
    fun provideAddedLinksPresenter(
        schedulers: Schedulers,
        profileApi: ProfileApi,
        linksInteractor: LinksInteractor
    ) = ProfileLinksPresenter(schedulers, profileApi, linksInteractor)
}
