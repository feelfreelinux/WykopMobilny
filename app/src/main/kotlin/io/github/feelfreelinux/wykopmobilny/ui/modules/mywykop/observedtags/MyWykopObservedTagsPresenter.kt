package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class MyWykopObservedTagsPresenter(val schedulers: Schedulers, val tagsApi: TagApi) : BasePresenter<MyWykopObservedTagsView>() {
    fun loadTags() {
        compositeObservable.add(
                tagsApi.getObservedTags()
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showTags(it) }, { view?.showErrorDialog(it) })
        )
    }
}