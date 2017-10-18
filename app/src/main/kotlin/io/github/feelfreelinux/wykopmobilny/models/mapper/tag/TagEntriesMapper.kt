package io.github.feelfreelinux.wykopmobilny.models.mapper.tag

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagEntries
import io.github.feelfreelinux.wykopmobilny.models.mapper.EntryMapper
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.TagEntriesResponse

class TagEntriesMapper {
    companion object : Mapper<TagEntriesResponse, TagEntries> {
        override fun map(value: TagEntriesResponse): TagEntries {
            value.run {
                return TagEntries(TagMetaMapper.map(meta), items.map { EntryMapper.map(it) })
            }
        }
    }
}