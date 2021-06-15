package io.github.wykopmobilny.storage.api

interface Storages {

    fun settingsPreferences(): SettingsPreferencesApi

    fun linksPreferences(): LinksPreferencesApi

    fun blacklistPreferences(): BlacklistPreferencesApi

    fun sessionStorage(): SessionStorage

    fun userInfoStorage(): UserInfoStorage
}
