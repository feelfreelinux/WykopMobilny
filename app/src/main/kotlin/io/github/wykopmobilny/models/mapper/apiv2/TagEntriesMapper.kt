package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.TagEntriesResponse
import io.github.wykopmobilny.models.dataclass.TagEntries

object TagEntriesMapper {

    fun map(value: TagEntriesResponse, owmContentFilter: OWMContentFilter) =
        TagEntries(
            entries = value.data.orEmpty().map { EntryMapper.map(it, owmContentFilter) },
            meta = value.meta,
        )
}
