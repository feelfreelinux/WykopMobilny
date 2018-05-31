package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class ActionsFragmentModule {
    @Provides
    fun providePresenter(schedulers: Schedulers, profileApi: ProfileApi, entriesInteractor: EntriesInteractor, entriesApi: EntriesApi, linksInteractor: LinksInteractor) = ActionsFragmentPresenter(schedulers, profileApi, entriesInteractor, linksInteractor, entriesApi)
}