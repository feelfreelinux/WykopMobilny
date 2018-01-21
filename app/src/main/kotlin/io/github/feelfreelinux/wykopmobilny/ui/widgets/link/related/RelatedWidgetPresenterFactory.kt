package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import javax.inject.Inject

class RelatedWidgetPresenterFactory @Inject constructor(val schedulers: Schedulers, val linksApi: LinksApi) {
    fun create() : RelatedWidgetPresenter {
        return RelatedWidgetPresenter(schedulers, linksApi)
    }
}