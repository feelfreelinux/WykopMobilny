package io.github.wykopmobilny.ui.modules.mikroblog.entry

import dagger.Module
import dagger.Provides
import io.github.wykopmobilny.api.entries.EntriesApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.wykopmobilny.ui.fragments.entrycomments.EntryCommentInteractor
import io.github.wykopmobilny.ui.modules.NewNavigator
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandler
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

@Module
class EntryDetailModule {

    @Provides
    fun providesEntryDetailPresenter(
        schedulers: Schedulers,
        entriesApi: EntriesApi,
        entriesInteractor: EntriesInteractor,
        commentInteractor: EntryCommentInteractor
    ) = EntryDetailPresenter(schedulers, entriesApi, entriesInteractor, commentInteractor)

    @Provides
    fun provideNavigator(activity: EntryActivity): NewNavigatorApi = NewNavigator(activity)

    @Provides
    fun provideLinkHandler(activity: EntryActivity, navigator: NewNavigatorApi): WykopLinkHandlerApi = WykopLinkHandler(activity, navigator)
}
