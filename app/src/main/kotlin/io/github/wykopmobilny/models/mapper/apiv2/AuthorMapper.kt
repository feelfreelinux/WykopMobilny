package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.AuthorResponse
import io.github.wykopmobilny.models.dataclass.Author
import io.github.wykopmobilny.models.mapper.Mapper

object AuthorMapper : Mapper<AuthorResponse, Author> {
    override fun map(value: AuthorResponse) =
        Author(value.login, value.avatar, value.color, value.sex ?: "")
}
