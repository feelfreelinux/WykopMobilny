package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.RelatedResponse
import io.github.wykopmobilny.models.dataclass.Related
import io.github.wykopmobilny.models.mapper.Mapper

object RelatedMapper : Mapper<RelatedResponse, Related> {

    override fun map(value: RelatedResponse) =
        Related(
            id = value.id,
            url = value.url,
            voteCount = value.voteCount,
            author = value.author?.let(AuthorMapper::map),
            title = value.title,
            userVote = value.userVote ?: 0
        )
}
