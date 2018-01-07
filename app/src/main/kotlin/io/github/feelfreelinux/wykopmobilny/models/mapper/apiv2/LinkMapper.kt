package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse

class LinkMapper {
    companion object : Mapper<LinkResponse, Link> {
        override fun map(value: LinkResponse): Link {
            return Link(value.id,
                    value.title,
                    value.description, value.tags, value.sourceUrl,
                    value.voteCount, emptyList(),
                    value.commentsCount, value.relatedCount,
                    if (value.author != null) AuthorMapper.map(value.author) else null, value.date, value.preview,
                    value.plus18, value.canVote, value.isHot,
                    value.status, value.userVote, value.app)
        }
    }
}