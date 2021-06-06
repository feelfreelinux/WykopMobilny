package io.github.wykopmobilny.ui.modules.tag.links

import io.github.wykopmobilny.api.responses.TagMetaResponse
import io.github.wykopmobilny.ui.fragments.links.LinksFragmentView

interface TagLinksView : LinksFragmentView {
    fun setParentMeta(tagMetaResponse: TagMetaResponse)
}
