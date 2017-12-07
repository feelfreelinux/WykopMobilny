package io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class PromotedPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val linksApi: LinksApi) : BasePresenter<PromotedView>() {
    var page = 1
    fun getPromotedLinks(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelperApi.subscribe(linksApi.getPromoted(page),
                {
                    if (it.isNotEmpty()) {
                        page++
                        view?.addDataToAdapter(it, shouldRefresh)
                    } else view?.disableLoading()
                },
                { view?.showErrorDialog(it) }, this)
    }
}