package io.github.wykopmobilny.ui.modules.mywykop.index

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.api.mywykop.MyWykopApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor

@Module
class MyWykopEntryLinkFragmentModule {
    @Provides
    fun provideMyWykopIndexFragmentPresenter(
        schedulers: Schedulers,
        myWykopApi: MyWykopApi,
        linksInteractor: LinksInteractor,
        entriesInteractor: EntriesInteractor,
        entriesApi: EntriesApi,
        linksApi: LinksApi
    ) =
        MyWykopEntryLinkPresenter(schedulers, entriesApi, entriesInteractor, linksInteractor, linksApi, myWykopApi)
}
