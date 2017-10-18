package io.github.feelfreelinux.wykopmobilny.models.mapper

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Comment
import io.github.feelfreelinux.wykopmobilny.models.pojo.CommentResponse

class CommentMapper {
    companion object : Mapper<CommentResponse, Comment> {
        override fun map(value: CommentResponse): Comment {
            value.run {
                return Comment(
                        id,
                        entryId,
                        Author(
                                author,
                                authorAvatarMed,
                                authorGroup,
                                authorSex,
                                app
                        ),
                        body,
                        date,
                        userVote > 0,
                        embed,
                        voters.map { VoterMapper.map(it) },
                        voteCount
                )
            }
        }
    }
}