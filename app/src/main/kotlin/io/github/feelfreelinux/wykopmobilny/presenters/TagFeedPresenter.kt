package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.TagFeedEntries
import io.github.feelfreelinux.wykopmobilny.utils.ApiResponseCallback
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

class TagFeedPresenter(val tag : String, wam: WykopApiManager, callbacks: FeedViewCallbacks) : FeedPresenter(wam, callbacks) {
    var resultCallback : ApiResponseCallback = {
        result ->
        run {
            val tagResults = result as TagFeedEntries
            entryList.addAll(tagResults.items)
            dataLoadedListener.invoke()
        }
    }

    override fun loadData(page : Int) {
        // For refresh actions, etc we should empty whole list
        if (page == 1) entryList.clear()
        wam.getTagEntries(page, tag, resultCallback)
    }
}