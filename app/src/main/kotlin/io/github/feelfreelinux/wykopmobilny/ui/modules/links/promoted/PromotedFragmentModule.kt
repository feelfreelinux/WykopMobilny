package io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class PromotedFragmentModule {
    @Provides
    fun providePromotedPresenter(
        schedulers: Schedulers,
        linksApi: LinksApi,
        linksInteractor: LinksInteractor
    ) = PromotedPresenter(schedulers, linksApi, linksInteractor)
}