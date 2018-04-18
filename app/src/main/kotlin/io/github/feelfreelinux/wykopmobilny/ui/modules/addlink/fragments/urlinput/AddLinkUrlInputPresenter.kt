package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.urlinput

import io.github.feelfreelinux.wykopmobilny.api.addlink.AddlinkApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class AddLinkUrlInputPresenter(val schedulers: Schedulers, val addlinkApi: AddlinkApi) : BasePresenter<AddLinkUrlInputFragmentView>() {
    fun createDraft(url: String) {
        view?.showDuplicatesLoading(true)
        compositeObservable.add(
                addlinkApi.getDraft(url)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.setLinkDraft(it)
                            view?.showDuplicatesLoading(false)
                        }, {
                            view?.showErrorDialog(it)
                            view?.showDuplicatesLoading(false)
                        })
        )
    }
}