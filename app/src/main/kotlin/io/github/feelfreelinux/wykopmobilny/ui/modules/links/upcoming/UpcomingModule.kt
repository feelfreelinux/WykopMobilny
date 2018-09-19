package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class UpcomingModule {
    @Provides
    fun provideUpcomingPresenter(schedulers: Schedulers, linksApi: LinksApi, linksInteractor: LinksInteractor) =
        UpcomingPresenter(schedulers, linksInteractor, linksApi)
}