package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries

import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class TagEntriesPresenter(val subscriptionHelper: SubscriptionHelperApi, val tagApi : TagApi) : BasePresenter<TagEntriesView>() {
    var page = 1
    var tag = ""

    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelper.subscribe(tagApi.getTagEntries(tag, page),
                {
                    view?.setMeta(it.meta)
                    if (it.entries.isNotEmpty()) {
                        page ++
                        view?.addDataToAdapter(it.entries, shouldRefresh)
                    } else view?.disableLoading()
                },
                { view?.showErrorDialog(it) }, this
        )
    }

    fun blockTag() {
        subscriptionHelper.subscribe(tagApi.block(tag),
                {
                    view?.setObserveState(it)
                },
                { view?.showErrorDialog(it) }, this
        )
    }

    fun unblockTag() {
        subscriptionHelper.subscribe(tagApi.unblock(tag),
                {
                    view?.setObserveState(it)
                },
                { view?.showErrorDialog(it) }, this
        )
    }

    fun observeTag() {
        subscriptionHelper.subscribe(tagApi.observe(tag),
                {
                    view?.setObserveState(it)
                },
                { view?.showErrorDialog(it) }, this
        )
    }

    fun unobserveTag() {
        subscriptionHelper.subscribe(tagApi.unobserve(tag),
                {
                    view?.setObserveState(it)
                },
                { view?.showErrorDialog(it) }, this
        )
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}