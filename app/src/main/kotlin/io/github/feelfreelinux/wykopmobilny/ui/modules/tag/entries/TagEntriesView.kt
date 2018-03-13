package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.entries

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.BaseEntryFeedView

interface TagEntriesView : BaseEntryFeedView {
    fun setParentMeta(tagMeta: TagMetaResponse)
}