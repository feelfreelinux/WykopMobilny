package io.github.feelfreelinux.wykopmobilny.ui.modules.addlink.fragments.urlinput

import io.github.feelfreelinux.wykopmobilny.api.addlink.AddLinkApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class AddLinkUrlInputPresenter(
    val schedulers: Schedulers,
    private val addLinkApi: AddLinkApi
) : BasePresenter<AddLinkUrlInputFragmentView>() {

    fun createDraft(url: String) {
        view?.showDuplicatesLoading(true)
        addLinkApi.getDraft(url)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.setLinkDraft(it)
                    view?.showDuplicatesLoading(false)
                },
                {
                    view?.showErrorDialog(it)
                    view?.showDuplicatesLoading(false)
                }
            )
            .intoComposite(compositeObservable)
    }
}
