package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag

import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class TagPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val tagApi: TagApi) : BasePresenter<TagView>(), BaseFeedPresenter {
    var tag = ""
    override fun loadData(page : Int) {
        subscriptionHelper.subscribe(tagApi.getTagEntries(tag, page),
                { view?.addDataToAdapter(it.entries, page == 1) },
                { view?.showErrorDialog(it) }, this
        )
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}