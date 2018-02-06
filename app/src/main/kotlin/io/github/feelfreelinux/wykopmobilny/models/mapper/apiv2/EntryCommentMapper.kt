package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryCommentResponse
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate

class EntryCommentMapper {
    companion object : Mapper<EntryCommentResponse, EntryComment> {
        override fun map(value: EntryCommentResponse): EntryComment {
            return EntryComment(
                    value.id,
                    value.entryId ?: 0,
                    AuthorMapper.map(value.author),
                    value.body ?: "",
                    value.date.toPrettyDate(),
                    value.userVote > 0,
                    if (value.embed != null) EmbedMapper.map(value.embed) else null,
                    value.voteCount,
                    value.app,
                    value.body?.toLowerCase()?.contains("#nsfw") ?: false
            )
        }
    }
}