package io.github.wykopmobilny.ui.modules.favorite.links

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class LinksFavoriteFragmentModule {
    @Provides
    fun provideLinksFavoritePresenter(schedulers: Schedulers, linksApi: LinksApi, linksInteractor: LinksInteractor) =
        LinksFavoritePresenter(schedulers, linksApi, linksInteractor)
}
