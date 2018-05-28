package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.links

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class LinksFavoriteFragmentModule {
    @Provides
    fun provideLinksFavoritePresenter(schedulers: Schedulers, linksApi: LinksApi, linksInteractor: LinksInteractor) = LinksFavoritePresenter(schedulers, linksApi, linksInteractor)
}