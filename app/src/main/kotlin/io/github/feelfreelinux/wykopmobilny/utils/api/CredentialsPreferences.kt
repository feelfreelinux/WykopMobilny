package io.github.feelfreelinux.wykopmobilny.utils.api

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.utils.preferences.Preferences

interface CredentialsPreferencesApi {
    var login: String?
    var userKey: String?
    var userToken: String?
    var avatarUrl: String?
    var backgroundUrl: String?
    var timeStamp: String?
}

class CredentialsPreferences(context: Context) : Preferences(context), CredentialsPreferencesApi {
    override var login by stringPref(defaultValue = "")
    override var userKey by stringPref(defaultValue = "")
    override var userToken by stringPref(defaultValue = "")
    override var avatarUrl by stringPref(defaultValue = "")
    override var backgroundUrl by stringPref(defaultValue = "")
    override var timeStamp by stringPref(defaultValue = "")
}
