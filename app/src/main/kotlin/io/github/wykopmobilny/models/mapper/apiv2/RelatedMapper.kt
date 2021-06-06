package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.responses.RelatedResponse
import io.github.wykopmobilny.models.dataclass.Related
import io.github.wykopmobilny.models.mapper.Mapper

class RelatedMapper {
    companion object : Mapper<RelatedResponse, Related> {
        override fun map(value: RelatedResponse) =
            Related(
                value.id,
                value.url,
                value.voteCount,
                if (value.author != null) AuthorMapper.map(value.author!!) else null,
                value.title,
                value.userVote ?: 0
            )
    }
}
