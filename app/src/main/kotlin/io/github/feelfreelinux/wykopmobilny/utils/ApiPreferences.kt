package io.github.feelfreelinux.wykopmobilny.utils


class ApiPreferences : Preferences() {
    var login by stringPref(defaultValue = "")
    var userKey by stringPref(defaultValue = "")
    fun isUserAuthorized(): Boolean {
        return !login.isNullOrBlank() and !userKey.isNullOrBlank()
    } //todo move to some kind of validator
}