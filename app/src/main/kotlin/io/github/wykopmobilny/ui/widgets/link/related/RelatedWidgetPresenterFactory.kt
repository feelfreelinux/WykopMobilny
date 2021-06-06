package io.github.wykopmobilny.ui.widgets.link.related

import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi
import javax.inject.Inject

class RelatedWidgetPresenterFactory @Inject constructor(
    val schedulers: Schedulers,
    val linksApi: LinksApi,
    val linkHandlerApi: WykopLinkHandlerApi
) {
    fun create() = RelatedWidgetPresenter(schedulers, linksApi, linkHandlerApi)
}
