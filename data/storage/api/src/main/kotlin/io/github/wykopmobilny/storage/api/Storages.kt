package io.github.wykopmobilny.storage.api

interface Storages {

    fun linksPreferences(): LinksPreferencesApi

    fun blacklistPreferences(): BlacklistPreferencesApi

    fun sessionStorage(): SessionStorage

    fun userInfoStorage(): UserInfoStorage

    fun userPreferences(): UserPreferenceApi
}
