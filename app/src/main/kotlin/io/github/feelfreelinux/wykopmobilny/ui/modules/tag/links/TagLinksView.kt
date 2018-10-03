package io.github.feelfreelinux.wykopmobilny.ui.modules.tag.links

import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.TagMetaResponse
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksFragmentView

interface TagLinksView : LinksFragmentView {
    fun setParentMeta(tagMetaResponse: TagMetaResponse)
}