package io.github.wykopmobilny.models.mapper.apiv2

import io.github.wykopmobilny.api.filters.OWMContentFilter
import io.github.wykopmobilny.api.responses.LinkResponse
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.utils.api.stripImageCompression
import io.github.wykopmobilny.utils.textview.removeHtml

object LinkMapper {

    fun map(value: LinkResponse, owmContentFilter: OWMContentFilter) =
        owmContentFilter.filterLink(
            Link(
                id = value.id,
                title = value.title?.removeHtml().orEmpty(),
                description = value.description?.removeHtml().orEmpty(),
                tags = value.tags,
                sourceUrl = value.sourceUrl,
                voteCount = value.voteCount,
                buryCount = value.buryCount,
                comments = mutableListOf(),
                commentsCount = value.commentsCount,
                relatedCount = value.relatedCount,
                author = value.author?.let(AuthorMapper::map),
                fullDate = value.date,
                preview = value.preview?.stripImageCompression(),
                plus18 = value.plus18,
                canVote = value.canVote,
                isHot = value.isHot,
                status = value.status,
                userVote = value.userVote,
                userFavorite = value.userFavorite ?: false,
                app = value.app,
                gotSelected = false,
                isBlocked = false,
            )
        )
}
