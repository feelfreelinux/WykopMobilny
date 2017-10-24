package io.github.feelfreelinux.wykopmobilny.models.mapper

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.pojo.Voter

class VoterMapper {
    companion object : Mapper<Voter, Author> {
        override fun map(value: Voter): Author {
            return Author(value.author, value.authorAvatarMed, value.authorGroup, value.authorSex, null)
        }
    }
}