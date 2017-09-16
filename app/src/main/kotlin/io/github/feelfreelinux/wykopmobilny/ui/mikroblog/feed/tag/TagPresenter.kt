package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag

import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter
import io.github.feelfreelinux.wykopmobilny.api.enqueue

class TagPresenter(private val tagApi : TagApi) : BasePresenter<TagView>(), BaseFeedPresenter {
    var tag = ""
    override fun loadData(page : Int) {
        tagApi.getTagEntries(tag, page).enqueue(
                { view?.addDataToAdapter(it.body()!!.items, page == 1)  },
                { view?.showErrorDialog(it) }
        )
    }
}