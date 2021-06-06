package io.github.wykopmobilny.ui.modules.mywykop.observedtags

import io.github.wykopmobilny.api.responses.ObservedTagResponse
import io.github.wykopmobilny.base.BaseView

interface MyWykopObservedTagsView : BaseView {
    fun showTags(tags: List<ObservedTagResponse>)
}
