package io.github.wykopmobilny.storage.api

import kotlinx.coroutines.flow.Flow

interface BlacklistPreferencesApi {
    var blockedTags: Set<String>?
    var blockedUsers: Set<String>?

    val blacklist: Flow<Blacklist?>

    suspend fun updateBlacklist(newValue: Blacklist?)
}

data class Blacklist(
    val tags: Set<String>,
    val users: Set<String>,
)
