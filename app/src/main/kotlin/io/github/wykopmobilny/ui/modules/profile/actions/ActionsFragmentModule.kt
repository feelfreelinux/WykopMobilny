package io.github.wykopmobilny.ui.modules.profile.actions

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class ActionsFragmentModule {
    @Provides
    fun providePresenter(
        schedulers: Schedulers,
        profileApi: ProfileApi,
        entriesInteractor: EntriesInteractor,
        entriesApi: EntriesApi,
        linksInteractor: LinksInteractor
    ) = ActionsFragmentPresenter(schedulers, profileApi, entriesInteractor, linksInteractor, entriesApi)
}
