package io.github.wykopmobilny.ui.modules.links.promoted

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class PromotedFragmentModule {
    @Provides
    fun providePromotedPresenter(
        schedulers: Schedulers,
        linksApi: LinksApi,
        linksInteractor: LinksInteractor
    ) = PromotedPresenter(schedulers, linksApi, linksInteractor)
}
