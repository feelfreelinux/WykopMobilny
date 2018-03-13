package io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class PromotedFragmentModule {
    @Provides
    fun providePromotedPresenter(schedulers: Schedulers, linksApi: LinksApi) =
            PromotedPresenter(schedulers, linksApi)
}