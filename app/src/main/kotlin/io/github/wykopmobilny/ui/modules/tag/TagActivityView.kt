package io.github.wykopmobilny.ui.modules.tag

import io.github.wykopmobilny.api.responses.ObserveStateResponse
import io.github.wykopmobilny.api.responses.TagMetaResponse
import io.github.wykopmobilny.base.BaseView

interface TagActivityView : BaseView {
    fun setMeta(tagMeta: TagMetaResponse)
    fun setObserveState(tagState: ObserveStateResponse)
}
