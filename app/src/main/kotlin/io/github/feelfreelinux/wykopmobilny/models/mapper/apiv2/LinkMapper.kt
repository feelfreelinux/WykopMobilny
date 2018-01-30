package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkResponse
import io.github.feelfreelinux.wykopmobilny.utils.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.api.stripImageCompression
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate

class LinkMapper {
    companion object {
        fun map(value: LinkResponse, linksPreferencesApi: LinksPreferencesApi): Link {
            return Link(value.id,
                    value.title.removeHtml(),
                    value.description?.removeHtml() ?: "", value.tags, value.sourceUrl,
                    value.voteCount, value.buryCount, emptyList(),
                    value.commentsCount, value.relatedCount,
                    if (value.author != null) AuthorMapper.map(value.author) else null, value.date.toPrettyDate(), value.preview?.stripImageCompression(),
                    value.plus18, value.canVote, value.isHot,
                    value.status, value.userVote, value.userFavorite ?: false, value.app, linksPreferencesApi.readLinksIds.contains("link_${value.id}"))
        }
    }
}