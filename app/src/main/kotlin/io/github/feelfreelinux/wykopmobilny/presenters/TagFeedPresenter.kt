package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.objects.TagFeedEntries
import io.github.feelfreelinux.wykopmobilny.utils.ApiResponseCallback
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

class TagFeedPresenter(val tag: String, view: FeedPresenter.View, wam: WykopApiManager) : FeedPresenter(view, wam) {
    override fun loadData(page: Int) {
        val resultCallback: ApiResponseCallback = {
            val tagResults = it as TagFeedEntries
            view.addDataToAdapter(tagResults.items.asList(), page == 0)
        }

        wam.getTagEntries(page, tag, resultCallback)
    }
}