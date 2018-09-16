package io.github.feelfreelinux.wykopmobilny.utils.usermanager

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LoginResponse
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.UserNotLoggedInDialog
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi

data class LoginCredentials(val login: String, val token: String)

data class UserCredentials(val login: String, val avatarUrl: String, val backgroundUrl: String?, val userKey: String)

interface UserManagerApi {
    fun loginUser(credentials: LoginCredentials)
    fun logoutUser()
    fun saveCredentials(credentials: LoginResponse)
    fun getUserCredentials(): UserCredentials?
    fun isUserAuthorized(): Boolean
    fun runIfLoggedIn(context: Context, callback: () -> Unit)
}

class UserManager(private val credentialsPreferencesApi: CredentialsPreferencesApi) : UserManagerApi {
    override fun loginUser(credentials: LoginCredentials) {
        credentialsPreferencesApi.apply {
            login = credentials.login
            userKey = credentials.token
        }
    }

    override fun logoutUser() {
        credentialsPreferencesApi.apply {
            login = ""
            userToken = ""
            userKey = ""
        }
    }

    override fun saveCredentials(credentials: LoginResponse) {
        credentialsPreferencesApi.apply {
            avatarUrl = credentials.profile.avatar
            backgroundUrl = credentials.profile.background
            userToken = credentials.userkey
        }
    }

    override fun isUserAuthorized(): Boolean {
        credentialsPreferencesApi.run {
            return !login.isNullOrBlank() and !userKey.isNullOrBlank()
        }
    }

    override fun getUserCredentials(): UserCredentials? {
        credentialsPreferencesApi.run {
            return if (!login.isNullOrEmpty() || !avatarUrl.isNullOrEmpty() || !userToken.isNullOrEmpty()) {
                UserCredentials(login!!, avatarUrl!!, backgroundUrl, userToken!!)
            } else null
        }
    }

    override fun runIfLoggedIn(context: Context, callback: () -> Unit) {
        if (isUserAuthorized()) {
            callback.invoke()
        } else UserNotLoggedInDialog(context)?.show()
    }
}