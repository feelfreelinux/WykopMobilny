package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries

import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class TagEntriesPresenter(val schedulers: Schedulers, val tagApi : TagApi) : BasePresenter<TagEntriesView>() {
    var page = 1
    var tag = ""

    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                tagApi.getTagEntries(tag, page)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.setMeta(it.meta)
                                    if (it.entries.isNotEmpty()) {
                                        page ++
                                        view?.addDataToAdapter(it.entries, shouldRefresh)
                                    } else view?.disableLoading()
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

    fun blockTag() {
        compositeObservable.add(
                tagApi.block(tag)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.setObserveState(it)
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

    fun unblockTag() {
        compositeObservable.add(
                tagApi.unblock(tag)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.setObserveState(it)
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

    fun observeTag() {
        compositeObservable.add(
                tagApi.observe(tag)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.setObserveState(it)
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

    fun unobserveTag() {
        compositeObservable.add(
                tagApi.unobserve(tag)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.setObserveState(it)
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }
}