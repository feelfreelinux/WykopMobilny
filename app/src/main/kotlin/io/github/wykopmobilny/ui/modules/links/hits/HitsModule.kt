package io.github.wykopmobilny.ui.modules.links.hits

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.hits.HitsApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class HitsModule {
    @Provides
    fun provideHitsPresenter(schedulers: Schedulers, hitsApi: HitsApi, linkInteractor: LinksInteractor) = HitsPresenter(
        schedulers,
        linkInteractor,
        hitsApi
    )
}
