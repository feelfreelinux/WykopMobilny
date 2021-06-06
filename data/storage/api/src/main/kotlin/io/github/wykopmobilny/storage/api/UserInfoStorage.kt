package io.github.wykopmobilny.storage.api

import kotlinx.coroutines.flow.StateFlow

interface UserInfoStorage {

    val loggedUser: StateFlow<LoggedUserInfo?>

    suspend fun updateLoggedUser(value: LoggedUserInfo?)
}

data class LoggedUserInfo(
    val userName: String,
    val userToken: String,
    val avatarUrl: String,
    val backgroundUrl: String?,
)
