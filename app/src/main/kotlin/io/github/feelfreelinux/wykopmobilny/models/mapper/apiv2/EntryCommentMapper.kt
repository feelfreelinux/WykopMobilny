package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryCommentResponse

class EntryCommentMapper {
    companion object {
        fun map(value: EntryCommentResponse, owmContentFilter: OWMContentFilter) =
            owmContentFilter.filterEntryComment(
                EntryComment(
                    value.id,
                    value.entryId ?: 0,
                    AuthorMapper.map(value.author),
                    value.body ?: "",
                    value.date,
                    value.userVote > 0,
                    if (value.embed != null) EmbedMapper.map(value.embed) else null,
                    value.voteCount,
                    value.app,
                    value.violationUrl ?: "",
                    value.body?.toLowerCase()?.contains("#nsfw") ?: false,
                    value.blocked
                )
            )
    }
}
