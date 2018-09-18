package io.github.feelfreelinux.wykopmobilny.ui.modules.links.downvoters

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class DownvotersPresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi
) : BasePresenter<DownvotersView>() {

    var linkId = -1

    fun getDownvoters() {
        compositeObservable.add(
            linksApi.getDownvoters(linkId)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.showDownvoters(it) }, { view?.showErrorDialog(it) })
        )
    }
}