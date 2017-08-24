package io.github.feelfreelinux.wykopmobilny.utils.api

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.utils.Preferences

class ApiPreferences(context: Context) : Preferences(context) {
    var login by stringPref(defaultValue = "")
    var userKey by stringPref(defaultValue = "")
    var userToken by stringPref(defaultValue = "")
    var avatarUrl by stringPref(defaultValue = "")
    fun isUserAuthorized(): Boolean {
        return !login.isNullOrBlank() and !userKey.isNullOrBlank() and !userToken.isNullOrBlank() and !avatarUrl.isNullOrBlank()
    } //todo move to some kind of validator
}