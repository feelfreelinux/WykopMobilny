package io.github.wykopmobilny.storage.api

interface BlacklistPreferencesApi {
    var blockedTags: Set<String>
    var blockedUsers: Set<String>
    var scraperSession: String?
    var blockedImported: Boolean
}
