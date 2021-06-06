package io.github.wykopmobilny.ui.modules.addlink.fragments.urlinput

import io.github.wykopmobilny.api.addlink.AddLinkApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.utils.intoComposite

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
