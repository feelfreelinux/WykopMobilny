package io.github.wykopmobilny.storage.api

import kotlinx.coroutines.flow.Flow

interface BlacklistPreferencesApi {
    val blockedTags: Set<String>?
    val blockedUsers: Set<String>?

    val blacklist: Flow<Blacklist?>

    suspend fun update(updater: (Blacklist) -> Blacklist)

    suspend fun clear()
}

data class Blacklist(
    val tags: Set<String>,
    val users: Set<String>,
) {

    companion object {

        fun empty() = Blacklist(
            tags = emptySet(),
            users = emptySet(),
        )
    }
}
