package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.AuthorResponse

class AuthorMapper {
    companion object : Mapper<AuthorResponse, Author> {
        override fun map(value: AuthorResponse): Author {
            return Author(value.login, value.avatar, value.color, value.sex ?: "", null)
        }

    }
}