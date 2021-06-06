package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.EntryCommentResponse
import io.github.wykopmobilny.models.dataclass.EntryComment

class EntryCommentMapper {
    companion object {
        fun map(value: EntryCommentResponse, owmContentFilter: OWMContentFilter) =
            owmContentFilter.filterEntryComment(
                EntryComment(
                    value.id,
                    value.entryId ?: 0,
                    AuthorMapper.map(value.author),
                    value.body.orEmpty(),
                    value.date,
                    value.userVote > 0,
                    value.embed?.let(EmbedMapper::map),
                    value.voteCount,
                    value.app,
                    value.violationUrl ?: "",
                    value.body?.lowercase()?.contains("#nsfw") ?: false,
                    value.blocked,
                )
            )
    }
}
