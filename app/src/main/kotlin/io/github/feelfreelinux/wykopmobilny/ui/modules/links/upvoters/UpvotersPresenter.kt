package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upvoters

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class UpvotersPresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi
) : BasePresenter<UpvotersView>() {

    var linkId = -1

    fun getUpvoters() {
        linksApi.getUpvoters(linkId)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showUpvoters(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }
}