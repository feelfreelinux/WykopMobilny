package io.github.feelfreelinux.wykopmobilny.ui.modules.tag

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObserveStateResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse

interface TagActivityView : BaseView {
    fun setMeta(tagMeta: TagMetaResponse)
    fun setObserveState(tagState: ObserveStateResponse)
}