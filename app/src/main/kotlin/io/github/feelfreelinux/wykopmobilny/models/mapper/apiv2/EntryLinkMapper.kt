package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryLinkResponse

class EntryLinkMapper {
    companion object {
        fun map(value: EntryLinkResponse, owmContentFilter: OWMContentFilter) =
            EntryLink(
                if (value.link != null) LinkMapper.map(value.link, owmContentFilter) else null,
                if (value.entry != null) EntryMapper.map(value.entry, owmContentFilter) else null
            )
    }
}