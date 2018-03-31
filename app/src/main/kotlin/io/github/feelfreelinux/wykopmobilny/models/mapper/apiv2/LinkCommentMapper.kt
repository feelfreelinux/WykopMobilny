package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkCommentResponse
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.containsTagInBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate

class LinkCommentMapper {
    companion object {
        fun map(value: LinkCommentResponse, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi): LinkComment {
            return LinkComment(value.id, AuthorMapper.map(value.author), value.date.toPrettyDate(),
                    value.body, value.blocked,
                    value.favorite, value.voteCount,
                    value.voteCountPlus,
                    value.voteCount - value.voteCountPlus,
                    value.userVote, value.parentId, value.canVote,
                    value.linkId,
                    if (value.embed != null) EmbedMapper.map(value.embed) else null,
                    value.app, false, false, 0,
                    value.violationUrl ?: "",
                    value.body?.toLowerCase()?.contains("#nsfw") ?: false,
                    value.blocked ||
                            (blacklistPreferencesApi.blockedUsers.contains(value.author.login)) ||
                            blacklistPreferencesApi.blockedTags.containsTagInBody(value.body?.toLowerCase() ?: "") ||
                            settingsPreferencesApi.hideLowRangeAuthors && value.author.color == 0
            )
        }
    }
}