package io.github.wykopmobilny.storage.api

import kotlinx.coroutines.flow.StateFlow

interface SessionStorage {

    val session: StateFlow<UserSession?>

    suspend fun updateSession(value: UserSession?)
}

data class UserSession(
    val login: String,
    val token: String,
)
