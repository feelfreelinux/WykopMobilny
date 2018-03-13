package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.links

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse

interface TagLinksView : BaseView {
    fun addDataToAdapter(entryList : List<Link>, shouldClearAdapter : Boolean)
    fun disableLoading()
    fun setParentMeta(tagMetaResponse: TagMetaResponse)
}