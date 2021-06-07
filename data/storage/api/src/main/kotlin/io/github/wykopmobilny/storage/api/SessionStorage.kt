package io.github.wykopmobilny.storage.api

import kotlinx.coroutines.flow.Flow

interface SessionStorage {

    val session: Flow<UserSession?>

    suspend fun updateSession(value: UserSession?)
}

data class UserSession(
    val login: String,
    val token: String,
)
