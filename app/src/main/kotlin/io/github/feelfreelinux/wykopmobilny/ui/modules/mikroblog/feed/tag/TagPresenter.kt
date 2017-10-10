package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag

import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.BaseFeedPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class TagPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val tagApi: TagApi) : BasePresenter<TagView>(), BaseFeedPresenter {
    var page = 1
    var tag = ""

    override fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelper.subscribe(tagApi.getTagEntries(tag, page),
                {
                    if (it.entries.isNotEmpty()) {
                        page ++
                        view?.addDataToAdapter(it.entries, shouldRefresh)
                    }
                },
                { view?.showErrorDialog(it) }, this
        )
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}