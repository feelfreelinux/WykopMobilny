package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments.EntryCommentInteractor
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandler
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

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