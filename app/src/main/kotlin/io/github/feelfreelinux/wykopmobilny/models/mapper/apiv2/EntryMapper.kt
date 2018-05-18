package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.mapper.Mapper
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryResponse
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.containsTagInBody
import io.github.feelfreelinux.wykopmobilny.utils.toPrettyDate

class EntryMapper {
    companion object {
        fun map(value: EntryResponse, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi): Entry {
            return Entry(value.id, AuthorMapper.map(value.author),
                    value.body ?: "", value.date.toPrettyDate(),
                    value.userVote > 0, value.favorite,
                    if (value.survey != null) SurveyMapper.map(value.survey) else null,
                    if (value.embed != null) EmbedMapper.map(value.embed) else null,
                    value.voteCount,
                    value.commentsCount,
                    if (value.comments != null) value.comments.map { EntryCommentMapper.map(it, blacklistPreferencesApi, settingsPreferencesApi)}.toMutableList()  else mutableListOf<EntryComment>(),
                    value.app,
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