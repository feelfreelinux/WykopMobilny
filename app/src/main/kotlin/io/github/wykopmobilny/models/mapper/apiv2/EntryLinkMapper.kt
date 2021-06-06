package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.EntryLinkResponse
import io.github.wykopmobilny.models.dataclass.EntryLink

object EntryLinkMapper {

    fun map(value: EntryLinkResponse, owmContentFilter: OWMContentFilter) =
        EntryLink(
            link = value.link?.let { LinkMapper.map(it, owmContentFilter) },
            entry = value.entry?.let { EntryMapper.map(it, owmContentFilter) },
        )
}
