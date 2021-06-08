package io.github.wykopmobilny.storage.api

import kotlinx.coroutines.flow.Flow

interface UserInfoStorage {

    val loggedUser: Flow<LoggedUserInfo?>

    suspend fun updateLoggedUser(value: LoggedUserInfo?)
}

data class LoggedUserInfo(
    val userName: String,
    val userToken: String,
    val avatarUrl: String,
    val backgroundUrl: String?,
)
