package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import javax.inject.Inject

class CommentPresenterFactory @Inject constructor(
        val schedulers: Schedulers,
        val entriesApi: EntriesApi,
        val clipboardHelperApi: ClipboardHelperApi,
        val navigatorApi: NewNavigatorApi,
        val linkHandler: WykopLinkHandlerApi) {
    fun create() : CommentPresenter {
        return CommentPresenter(schedulers, entriesApi, navigatorApi, linkHandler)
    }
}