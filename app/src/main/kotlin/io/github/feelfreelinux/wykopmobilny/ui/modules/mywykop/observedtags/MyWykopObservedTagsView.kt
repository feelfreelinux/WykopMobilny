package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.observedtags

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse

interface MyWykopObservedTagsView : BaseView {
    fun showTags(tags: List<ObservedTagResponse>)
}