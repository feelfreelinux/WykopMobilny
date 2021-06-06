package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.TagLinksResponse
import io.github.wykopmobilny.models.dataclass.TagLinks

class TagLinksMapper {
    companion object {
        fun map(value: TagLinksResponse, owmContentFilter: OWMContentFilter) =
            TagLinks(value.data!!.map { LinkMapper.map(it, owmContentFilter) }, value.meta)
    }
}
