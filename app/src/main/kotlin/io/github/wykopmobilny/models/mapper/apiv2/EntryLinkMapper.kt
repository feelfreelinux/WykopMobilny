package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.EntryLinkResponse
import io.github.wykopmobilny.models.dataclass.EntryLink

class EntryLinkMapper {
    companion object {
        fun map(value: EntryLinkResponse, owmContentFilter: OWMContentFilter) =
            EntryLink(
                value.link?.let { LinkMapper.map(it, owmContentFilter) },
                value.entry?.let { EntryMapper.map(it, owmContentFilter) },
            )
    }
}
