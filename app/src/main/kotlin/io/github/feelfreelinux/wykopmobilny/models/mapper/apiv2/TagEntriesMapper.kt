package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagEntriesResponse

class TagEntriesMapper {
    companion object : Mapper<TagEntriesResponse, TagEntries> {
        override fun map(value: TagEntriesResponse): TagEntries {
            return TagEntries(value.data!!.map { EntryMapper.map(it) }, value.meta)
        }
    }
}