package io.github.feelfreelinux.wykopmobilny.utils.api

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.utils.Preferences

interface CredentialsPreferencesApi {
    var login: String?
    var userKey: String?
    var userToken: String?
    var avatarUrl: String?
    var timeStamp: String?
    fun isUserAuthorized(): Boolean
}

class CredentialsPreferences(context: Context) : Preferences(context), CredentialsPreferencesApi {
    override var login by stringPref(defaultValue = "")
    override var userKey by stringPref(defaultValue = "")
    override var userToken by stringPref(defaultValue = "")
    override var avatarUrl by stringPref(defaultValue = "")
    override var timeStamp by stringPref(defaultValue = "")

    override fun isUserAuthorized(): Boolean {
        return !login.isNullOrBlank() and !userKey.isNullOrBlank() and !userToken.isNullOrBlank() and !avatarUrl.isNullOrBlank()
    } //todo move to some kind of validator
}