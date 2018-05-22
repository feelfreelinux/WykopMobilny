package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.entries

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesFragmentView
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.BaseEntryFeedView

interface TagEntriesView : EntriesFragmentView {
    fun setParentMeta(tagMeta: TagMetaResponse)
}