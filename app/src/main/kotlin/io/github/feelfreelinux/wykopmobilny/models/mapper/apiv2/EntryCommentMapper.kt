package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Comment
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.EntryCommentResponse

class EntryCommentMapper {
    companion object : Mapper<EntryCommentResponse, Comment> {
        override fun map(value: EntryCommentResponse): Comment {
            return Comment(
                    value.id,
                    0,
                    AuthorMapper.map(value.author),
                    value.body,
                    value.date,
                    value.userVote > 0,
                    null,
                    emptyList(),
                    value.voteCount
            )
        }

    }
}