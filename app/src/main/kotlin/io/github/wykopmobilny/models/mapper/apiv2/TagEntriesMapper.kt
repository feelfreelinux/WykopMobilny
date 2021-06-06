package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.TagEntriesResponse
import io.github.wykopmobilny.models.dataclass.TagEntries

class TagEntriesMapper {
    companion object {
        fun map(value: TagEntriesResponse, owmContentFilter: OWMContentFilter) =
            TagEntries(value.data!!.map { EntryMapper.map(it, owmContentFilter) }, value.meta)
    }
}
