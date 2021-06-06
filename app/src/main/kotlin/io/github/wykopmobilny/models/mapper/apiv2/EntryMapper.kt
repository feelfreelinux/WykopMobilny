package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.EntryResponse
import io.github.wykopmobilny.models.dataclass.Entry

class EntryMapper {
    companion object {
        fun map(value: EntryResponse, owmContentFilter: OWMContentFilter) =
            owmContentFilter.filterEntry(
                Entry(
                    value.id, AuthorMapper.map(value.author),
                    value.body ?: "", value.date,
                    value.userVote > 0, value.favorite,
                    value.survey?.let(SurveyMapper.Companion::map),
                    value.embed?.let(EmbedMapper.Companion::map),
                    value.voteCount,
                    value.commentsCount,
                    value.comments.orEmpty().map { EntryCommentMapper.map(it, owmContentFilter) }.toMutableList(),
                    value.app,
                    value.violationUrl ?: "",
                    value.body?.lowercase()?.contains("#nsfw") ?: false,
                    value.blocked,
                    true,
                    value.isCommentingPossible,
                )
            )
    }
}
