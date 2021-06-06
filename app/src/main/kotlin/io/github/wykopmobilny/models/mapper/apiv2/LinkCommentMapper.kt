package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.AuthorResponse
import io.github.wykopmobilny.api.responses.LinkCommentResponse
import io.github.wykopmobilny.models.dataclass.LinkComment

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
                    value.embed?.let(EmbedMapper.Companion::map),
                    value.app, false, false, 0,
                    value.violationUrl ?: "",
                    value.body?.lowercase()?.contains("#nsfw") ?: false,
                    value.blocked
                )
            )
    }
}
