package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagMeta
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.BaseFeedView

interface TagView : BaseFeedView {
    fun setMeta(tagMeta: TagMeta)
}