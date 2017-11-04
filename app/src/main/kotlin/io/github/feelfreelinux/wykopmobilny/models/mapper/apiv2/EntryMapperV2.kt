package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.EntryResponseV2

class EntryMapperV2 {
    companion object : Mapper<EntryResponseV2, Entry> {
        override fun map(value: EntryResponseV2): Entry {
            return Entry(value.id, AuthorMapper.map(value.author),
                    value.body ?: "", value.date,
                    value.userVote > 0,  value.favorite,
                    null, value.voteCount, emptyList(),
                    value.voteCount,
                    if (value.comments != null) value.comments.map { EntryCommentMapper.map(it) } else emptyList()
                    )
        }

    }
}