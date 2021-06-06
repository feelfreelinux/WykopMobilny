package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.EntryResponse
import io.github.wykopmobilny.models.dataclass.Entry

object EntryMapper {

    fun map(value: EntryResponse, owmContentFilter: OWMContentFilter) =
        owmContentFilter.filterEntry(
            Entry(
                value.id, AuthorMapper.map(value.author),
                value.body.orEmpty(), value.date,
                value.userVote > 0, value.favorite,
                value.survey?.let(SurveyMapper::map),
                embed = value.embed?.let(EmbedMapper::map),
                voteCount = value.voteCount,
                commentsCount = value.commentsCount,
                comments = value.comments.orEmpty().map { EntryCommentMapper.map(it, owmContentFilter) }.toMutableList(),
                app = value.app,
                violationUrl = value.violationUrl.orEmpty(),
                isNsfw = value.body?.lowercase()?.contains("#nsfw") ?: false,
                isBlocked = value.blocked,
                collapsed = true,
                isCommentingPossible = value.isCommentingPossible,
            )
        )
}
