package io.github.feelfreelinux.wykopmobilny.ui.modules.links.hits

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.hits.HitsApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class HitsModule {
    @Provides
    fun provideHitsPresenter(schedulers: Schedulers, hitsApi: HitsApi, linkInteractor: LinksInteractor) = HitsPresenter(schedulers, hitsApi, linkInteractor)
}