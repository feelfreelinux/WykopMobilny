package io.github.feelfreelinux.wykopmobilny.models.mapper.apiv2

import io.github.feelfreelinux.wykopmobilny.models.dataclass.TagLinks
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.TagLinksResponse
import io.github.feelfreelinux.wykopmobilny.utils.preferences.BlacklistPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.LinksPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.preferences.SettingsPreferencesApi

class TagLinksMapper {
    companion object {
        fun map(value: TagLinksResponse, linksPreferencesApi: LinksPreferencesApi, blacklistPreferencesApi: BlacklistPreferencesApi, settingsPreferencesApi: SettingsPreferencesApi): TagLinks {
            return TagLinks(value.data!!.map { LinkMapper.map(it, linksPreferencesApi, blacklistPreferencesApi, settingsPreferencesApi) }, value.meta)
        }
    }
}