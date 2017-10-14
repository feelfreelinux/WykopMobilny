package io.github.feelfreelinux.wykopmobilny.utils.usermanager

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.models.pojo.Profile
import io.github.feelfreelinux.wykopmobilny.ui.dialogs.UserNotLoggedInDialog
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.printout

data class LoginCredentials(val login : String, val token : String)

data class UserCredentials(val login : String, val avatarUrl : String, val userKey : String)

interface UserManagerApi {
    fun loginUser(credentials : LoginCredentials)
    fun logoutUser()
    fun saveCredentials(user : Profile)
    fun getUserCredentials() : UserCredentials?
    fun isUserAuthorized() : Boolean
    fun runIfLoggedIn(context : Context, callback : () -> Unit)
}

class UserManager(private val credentialsPreferencesApi: CredentialsPreferencesApi) : UserManagerApi {
    override fun loginUser(credentials : LoginCredentials) {
        credentialsPreferencesApi.apply {
            login   = credentials.login
            userKey = credentials.token
        }
    }

    override fun logoutUser() {
        credentialsPreferencesApi.apply {
            login     = ""
            userToken = ""
            userKey   = ""
        }
    }

    override fun saveCredentials(user : Profile) {
        credentialsPreferencesApi.apply {
            avatarUrl = user.avatarBig
            userToken = user.userKey
        }
    }

    override fun isUserAuthorized() : Boolean {
        credentialsPreferencesApi.run {
            return !login.isNullOrBlank() and !userKey.isNullOrBlank()
        }
    }

    override fun getUserCredentials(): UserCredentials? {
        credentialsPreferencesApi.run {
            return if (!login.isNullOrEmpty() || !avatarUrl.isNullOrEmpty() || !userToken.isNullOrEmpty()) {
                UserCredentials(login!!, avatarUrl!!, userToken!!)
            } else null
        }
    }

    override fun runIfLoggedIn(context : Context, callback : () -> Unit) {
        if (isUserAuthorized()) {
            callback.invoke()
        } else UserNotLoggedInDialog(context)?.show()
    }
}