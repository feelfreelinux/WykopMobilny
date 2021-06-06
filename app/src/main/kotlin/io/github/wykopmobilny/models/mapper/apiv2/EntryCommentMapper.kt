package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.EntryCommentResponse
import io.github.wykopmobilny.models.dataclass.EntryComment

object EntryCommentMapper {

    fun map(value: EntryCommentResponse, owmContentFilter: OWMContentFilter) =
        owmContentFilter.filterEntryComment(
            EntryComment(
                id = value.id,
                entryId = value.entryId ?: 0,
                author = AuthorMapper.map(value.author),
                body = value.body.orEmpty(),
                fullDate = value.date,
                isVoted = value.userVote > 0,
                embed = value.embed?.let(EmbedMapper::map),
                voteCount = value.voteCount,
                app = value.app,
                violationUrl = value.violationUrl ?: "",
                isNsfw = value.body?.lowercase()?.contains("#nsfw") ?: false,
                isBlocked = value.blocked,
            )
        )
}
