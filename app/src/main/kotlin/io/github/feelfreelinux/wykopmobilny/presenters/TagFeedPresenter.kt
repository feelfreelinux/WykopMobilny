package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.objects.TagFeedEntries
import io.github.feelfreelinux.wykopmobilny.utils.ApiResponseCallback
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

class TagFeedPresenter(val tag : String, wam: WykopApiManager, callbacks: FeedViewCallbacks) : FeedPresenter(wam, callbacks) {
    override fun loadData(page : Int) {
        val resultCallback : ApiResponseCallback = {
            result ->
            run {
                // For refresh actions, etc we should empty whole list
                if (page == 1) entryList.clear()

                val tagResults = result as TagFeedEntries
                entryList.addAll(tagResults.items)
                dataLoadedListener.invoke(page == 1)
            }
        }

        wam.getTagEntries(page, tag, resultCallback)
    }
}