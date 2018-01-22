package io.github.feelfreelinux.wykopmobilny.ui.modules.tag

import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class TagActivityPresenter(val schedulers : Schedulers, val tagApi: TagApi) : BasePresenter<TagActivityView>() {
    lateinit var tag : String
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