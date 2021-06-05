package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagLinks
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagLinksResponse

class TagLinksMapper {
    companion object {
        fun map(value: TagLinksResponse, owmContentFilter: OWMContentFilter) =
            TagLinks(value.data!!.map { LinkMapper.map(it, owmContentFilter) }, value.meta)
    }
}
