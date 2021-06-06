package io.github.wykopmobilny.ui.modules.search.links

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.search.SearchApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class LinkSearchModule {
    @Provides
    fun provideLinkSearchPresenter(schedulers: Schedulers, searchApi: SearchApi, linksInteractor: LinksInteractor) =
        LinkSearchPresenter(schedulers, searchApi, linksInteractor)
}
