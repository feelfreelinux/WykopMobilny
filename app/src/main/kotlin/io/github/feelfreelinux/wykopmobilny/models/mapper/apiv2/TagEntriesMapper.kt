package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagEntriesResponse

class TagEntriesMapper {
    companion object {
        fun map(value: TagEntriesResponse, owmContentFilter: OWMContentFilter) =
            TagEntries(value.data!!.map { EntryMapper.map(it, owmContentFilter) }, value.meta)
    }
}