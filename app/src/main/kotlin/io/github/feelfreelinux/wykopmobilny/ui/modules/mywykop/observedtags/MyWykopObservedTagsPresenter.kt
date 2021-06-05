package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags

import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class MyWykopObservedTagsPresenter(
    val schedulers: Schedulers,
    private val tagsApi: TagApi
) : BasePresenter<MyWykopObservedTagsView>() {

    fun loadTags() {
        tagsApi.getObservedTags()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showTags(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }
}
