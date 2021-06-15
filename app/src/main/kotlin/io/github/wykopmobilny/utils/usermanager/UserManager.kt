package io.github.wykopmobilny.utils.usermanager

import android.content.Context
import io.github.wykopmobilny.api.responses.LoginResponse
import io.github.wykopmobilny.storage.api.LoggedUserInfo
import io.github.wykopmobilny.storage.api.SessionStorage
import io.github.wykopmobilny.storage.api.UserInfoStorage
import io.github.wykopmobilny.storage.api.UserSession
import io.github.wykopmobilny.ui.dialogs.userNotLoggedInDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

data class LoginCredentials(val login: String, val token: String)

data class UserCredentials(
    val login: String,
    val avatarUrl: String,
    val backgroundUrl: String?,
    val userKey: String
)

interface SimpleUserManagerApi {
    fun getUserCredentials(): UserCredentials?
}

interface UserManagerApi : SimpleUserManagerApi {
    fun loginUser(credentials: LoginCredentials)
    fun logoutUser()
    fun saveCredentials(credentials: LoginResponse)
    fun runIfLoggedIn(context: Context, callback: () -> Unit)
}

fun UserManagerApi.isUserAuthorized() = getUserCredentials() != null

class UserManager @Inject constructor(
    private val sessionStorage: SessionStorage,
    private val userInfoStorage: UserInfoStorage,
) : UserManagerApi {
    override fun loginUser(credentials: LoginCredentials) = runBlocking {
        sessionStorage.updateSession(
            value = UserSession(
                login = credentials.login,
                token = credentials.token,
            )
        )
    }

    override fun logoutUser() = runBlocking {
        sessionStorage.updateSession(null)
        userInfoStorage.updateLoggedUser(null)
    }

    override fun saveCredentials(credentials: LoginResponse) = runBlocking {
        userInfoStorage.updateLoggedUser(
            value = LoggedUserInfo(
                userName = credentials.profile.login,
                userToken = credentials.userkey,
                avatarUrl = credentials.profile.avatar,
                backgroundUrl = credentials.profile.background,
            )
        )
    }

    private fun isUserAuthorized(): Boolean = runBlocking { sessionStorage.session.first() } != null

    override fun getUserCredentials(): UserCredentials? =
        runBlocking {
            userInfoStorage.loggedUser.first()?.let {
                UserCredentials(
                    login = it.userName,
                    avatarUrl = it.avatarUrl,
                    backgroundUrl = it.backgroundUrl,
                    userKey = it.userToken,
                )
            }
        }

    override fun runIfLoggedIn(context: Context, callback: () -> Unit) {
        if (isUserAuthorized()) {
            callback.invoke()
        } else {
            userNotLoggedInDialog(context)?.show()
        }
    }
}
