package io.github.feelfreelinux.wykopmobilny.utils

import android.content.Context

interface SettingsPreferencesApi {
    var notificationsSchedulerDelay : String?
    var hotEntriesScreen : String?
    var defaultScreen : String?
    var linkImagePosition : String?
    var linkShowImage : Boolean
    var linkSimpleList : Boolean
    var linkShowAuthor : Boolean
    var showAdultContent : Boolean
    var hideNsfw: Boolean
    var useDarkTheme : Boolean
    var useAmoledTheme : Boolean
    var showNotifications : Boolean
    var showMinifiedImages : Boolean
    var cutImages : Boolean
    var openSpoilersDialog : Boolean
    var cutImageProportion : Int

}

class SettingsPreferences(context : Context) : Preferences(context, true), SettingsPreferencesApi {
    override var notificationsSchedulerDelay by stringPref(defaultValue = "15")
    override var showAdultContent by booleanPref(defaultValue = false)
    override var hideNsfw: Boolean by booleanPref(defaultValue = true)
    override var hotEntriesScreen by stringPref(defaultValue = "newest")
    override var defaultScreen by stringPref(defaultValue = "mainpage")
    override var linkImagePosition by stringPref(defaultValue = "left")
    override var linkShowImage by booleanPref(defaultValue = true)
    override var linkSimpleList by booleanPref(defaultValue = false)
    override var linkShowAuthor by booleanPref(defaultValue = false)
    override var useDarkTheme by booleanPref(defaultValue = false)
    override var useAmoledTheme by booleanPref(defaultValue = false)
    override var showMinifiedImages by booleanPref(defaultValue = false)
    override var cutImages by booleanPref(defaultValue = true)
    override var cutImageProportion by intPref(defaultValue = 60)
    override var openSpoilersDialog by booleanPref(defaultValue = true)
    override var showNotifications by booleanPref(defaultValue = true)
}