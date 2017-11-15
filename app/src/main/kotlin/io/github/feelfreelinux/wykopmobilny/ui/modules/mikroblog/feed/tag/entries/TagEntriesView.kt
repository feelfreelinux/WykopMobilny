package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.entries

import io.github.feelfreelinux.wykopmobilny.base.BaseNavigationView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.BaseEntryFeedView

interface TagEntriesView : BaseEntryFeedView {
    fun setMeta(tagMeta: TagMetaResponse)
    fun setObserveState(tagState : TagStateResponse)
}