package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context
import java.util.concurrent.TimeUnit

interface SettingsPreferencesApi {
    var notificationsSchedulerDelay : String?
    var hotEntriesScreen : String?
    var showAdultContent : Boolean
    var useDarkTheme : Boolean


}

class SettingsPreferences(context : Context) : Preferences(context, true), SettingsPreferencesApi {
    override var notificationsSchedulerDelay by stringPref(defaultValue = "15")
    override var showAdultContent by booleanPref(defaultValue = false)
    override var hotEntriesScreen by stringPref(defaultValue = "newest")
    override var useDarkTheme by booleanPref(defaultValue = false)
}