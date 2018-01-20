package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class UpcomingModule {
    @Provides
    fun provideUpcomingPresenter(schedulers: Schedulers, linksApi: LinksApi) = UpcomingPresenter(schedulers, linksApi)
}