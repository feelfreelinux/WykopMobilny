package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.EntryLinkResponse
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi

class EntryLinkMapper {
    companion object {
        fun map(value: EntryLinkResponse, linksPreferencesApi : LinksPreferencesApi, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi): EntryLink {
            return  EntryLink(
                    if (value.link != null) LinkMapper.map(value.link!!, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi) else null,
                    if (value.entry != null) EntryMapper.map(value.entry!!, blacklistPreferencesApi, settingsPreferencesApi) else null
            )
        }
    }
}