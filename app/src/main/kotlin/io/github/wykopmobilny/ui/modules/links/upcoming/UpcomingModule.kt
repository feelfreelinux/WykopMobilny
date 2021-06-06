package io.github.wykopmobilny.ui.modules.links.upcoming

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class UpcomingModule {
    @Provides
    fun provideUpcomingPresenter(schedulers: Schedulers, linksApi: LinksApi, linksInteractor: LinksInteractor) =
        UpcomingPresenter(schedulers, linksInteractor, linksApi)
}
