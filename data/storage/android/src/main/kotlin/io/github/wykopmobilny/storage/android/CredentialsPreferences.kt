package io.github.wykopmobilny.storage.android

import android.content.Context
import dagger.Reusable
import io.github.wykopmobilny.storage.api.LoggedUserInfo
import io.github.wykopmobilny.storage.api.SessionStorage
import io.github.wykopmobilny.storage.api.UserInfoStorage
import io.github.wykopmobilny.storage.api.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor
import javax.inject.Inject

@Reusable
internal class CredentialsPreferences @Inject constructor(
    context: Context,
    executor: Executor,
) : BasePreferences(context, executor), SessionStorage, UserInfoStorage {

    private var login by stringPref(key = "login")
    private var userKey by stringPref(key = "userKey")
    private var userToken by stringPref(key = "userToken")
    private var avatarUrl by stringPref(key = "avatarUrl")
    private var backgroundUrl by stringPref(key = "backgroundUrl")

    private var userSession: UserSession?
        get() {
            return UserSession(
                login = login ?: return null,
                token = userKey ?: return null,
            )
        }
        set(value) {
            login = value?.login
            userKey = value?.token
        }

    private var userInfo: LoggedUserInfo?
        get() {
            return LoggedUserInfo(
                userName = login ?: return null,
                userToken = userToken ?: return null,
                avatarUrl = avatarUrl ?: return null,
                backgroundUrl = backgroundUrl,
            )
        }
        set(value) {
            login = value?.userName
            userToken = value?.userToken
            avatarUrl = value?.avatarUrl
            backgroundUrl = value?.backgroundUrl
        }

    override val session =
        preferences.filter { it == "login" || it == "userKey" }
            .onStart { emit("") }
            .map { userSession }

    override suspend fun updateSession(value: UserSession?) = withContext(Dispatchers.IO) {
        userSession = value
    }

    override val loggedUser = preferences.filter { it in userInfoKeys }
        .onStart { emit("") }
        .map { userInfo }

    override suspend fun updateLoggedUser(value: LoggedUserInfo?) = withContext(Dispatchers.IO) {
        userInfo = value
    }

    companion object {
        private val userInfoKeys = listOf("login", "userToken", "avatarUrl", "backgroundUrl")
    }
}
