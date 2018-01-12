package io.github.feelfreelinux.wykopmobilny.di.modules

import dagger.Module
import dagger.Provides
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.EntryPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment.CommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment.LinkCommentPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Provider

@Module
class ViewPresenterFactoryModule {
    @Provides
    fun provideEntryPresenterFactory(schedulers: Schedulers, entriesApi: EntriesApi, clipboardHelperApi: ClipboardHelperApi, newNavigatorApi: NewNavigatorApi, wykopLinkHandlerApi: WykopLinkHandlerApi)
            = EntryPresenterFactory(Provider { schedulers }, Provider { entriesApi },   Provider { clipboardHelperApi },  Provider { newNavigatorApi },  Provider { wykopLinkHandlerApi })

    @Provides
    fun provideCommentPresenterFactory(schedulers: Schedulers, entriesApi: EntriesApi, clipboardHelperApi: ClipboardHelperApi, newNavigatorApi: NewNavigatorApi, wykopLinkHandlerApi: WykopLinkHandlerApi)
            = CommentPresenterFactory(Provider { schedulers }, Provider { entriesApi },   Provider { clipboardHelperApi },  Provider { newNavigatorApi },  Provider { wykopLinkHandlerApi })

    @Provides
    fun provideLinkCommentPresenterFactory(schedulers: Schedulers, linksApi: LinksApi, newNavigatorApi: NewNavigatorApi)
            = LinkCommentPresenterFactory(Provider { schedulers }, Provider { newNavigatorApi }, Provider { linksApi })

}