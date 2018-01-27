package io.github.feelfreelinux.wykopmobilny.ui.modules.links.hits

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.hits.HitsApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class HitsModule {
    @Provides
    fun provideHitsPresenter(schedulers: Schedulers, hitsApi: HitsApi) = HitsPresenter(schedulers, hitsApi)
}