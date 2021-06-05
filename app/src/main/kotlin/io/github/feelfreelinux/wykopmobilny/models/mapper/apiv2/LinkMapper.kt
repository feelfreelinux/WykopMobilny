package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.api.filters.OWMContentFilter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml

class LinkMapper {
    companion object {
        fun map(value: LinkResponse, owmContentFilter: OWMContentFilter) =
            owmContentFilter.filterLink(
                Link(
                    value.id,
                    value.title?.removeHtml() ?: "",
                    value.description?.removeHtml() ?: "",
                    value.tags,
                    value.sourceUrl,
                    value.voteCount,
                    value.buryCount,
                    mutableListOf(),
                    value.commentsCount,
                    value.relatedCount,
                    if (value.author != null) AuthorMapper.map(value.author) else null,
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
                    false
                )
            )
    }
}
