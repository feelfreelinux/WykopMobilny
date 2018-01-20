package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class LinksFavoriteFragmentModule {
    @Provides
    fun provideLinksFavoritePresenter(schedulers: Schedulers, linksApi: LinksApi) = LinksFavoritePresenter(schedulers, linksApi)
}