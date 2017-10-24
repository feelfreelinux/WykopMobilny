package io.github.feelfreelinux.wykopmobilny.models.mapper

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Comment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.pojo.EntryResponse

class EntryMapper{
    companion object : Mapper<EntryResponse, Entry> {
        override fun map(value: EntryResponse): Entry {
            val commentList = ArrayList<Comment>()

            value.run {
                comments?.mapTo(commentList, { CommentMapper.map(it) })

                return Entry(
                        id,
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
                        !(userFavorite == null || !userFavorite!!),
                        embed,
                        voteCount,
                        voters.map { VoterMapper.map(it) },
                        commentCount,
                        commentList.toList()
                )
            }
        }
    }
}