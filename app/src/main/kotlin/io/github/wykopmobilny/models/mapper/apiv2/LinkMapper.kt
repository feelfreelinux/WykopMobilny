package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.LinkResponse
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.utils.api.stripImageCompression
import io.github.wykopmobilny.utils.textview.removeHtml

class LinkMapper {
    companion object {
        fun map(value: LinkResponse, owmContentFilter: OWMContentFilter) =
            owmContentFilter.filterLink(
                Link(
                    value.id,
                    value.title?.removeHtml().orEmpty(),
                    value.description?.removeHtml().orEmpty(),
                    value.tags,
                    value.sourceUrl,
                    value.voteCount,
                    value.buryCount,
                    mutableListOf(),
                    value.commentsCount,
                    value.relatedCount,
                    value.author?.let(AuthorMapper.Companion::map),
                    value.date,
                    value.preview?.stripImageCompression(),
                    value.plus18,
                    value.canVote,
                    value.isHot,
                    value.status,
                    value.userVote,
                    value.userFavorite ?: false,
                    value.app,
                    false,
                    false,
                )
            )
    }
}
