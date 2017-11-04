package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagStateResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.BaseFeedView

interface TagView : BaseFeedView {
    fun setMeta(tagMeta: TagMetaResponse)
    fun setObserveState(tagState : TagStateResponse)
}