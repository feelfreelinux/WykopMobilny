package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.AuthorResponse
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse

class LinkCommentMapper {
    companion object {
        fun map(value: LinkCommentResponse, owmContentFilter: OWMContentFilter) =
            owmContentFilter.filterLinkComment(
                LinkComment(
                    value.id, AuthorMapper.map(value.author ?: AuthorResponse("", 9999, "", "")), value.date,
                    value.body, value.blocked,
                    value.favorite, value.voteCount,
                    value.voteCountPlus,
                    value.voteCount - value.voteCountPlus,
                    value.userVote, value.parentId, value.canVote,
                    value.linkId,
                    if (value.embed != null) EmbedMapper.map(value.embed) else null,
                    value.app, false, false, 0,
                    value.violationUrl ?: "",
                    value.body?.toLowerCase()?.contains("#nsfw") ?: false,
                    value.blocked
                )
            )
    }
}
