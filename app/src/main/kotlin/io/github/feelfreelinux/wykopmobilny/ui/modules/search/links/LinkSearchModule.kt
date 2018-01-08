package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

@Module
class LinkSearchModule {
    @Provides
    fun provideLinkSearchPresenter(schedulers: Schedulers, searchApi: SearchApi) = LinkSearchPresenter(schedulers, searchApi)
}