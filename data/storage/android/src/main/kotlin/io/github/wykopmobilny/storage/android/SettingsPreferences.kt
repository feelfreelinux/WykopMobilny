package io.github.wykopmobilny.storage.android

import android.content.Context
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import javax.inject.Inject

internal class SettingsPreferences @Inject constructor(
    context: Context
) : BasePreferences(context, true), SettingsPreferencesApi {

    override var notificationsSchedulerDelay by stringPref(key = "notificationsSchedulerDelay")
    override var showAdultContent by booleanPref(key = "showAdultContent", defaultValue = false)
    override var hideNsfw by booleanPref(key = "hideNsfw", defaultValue = true)
    override var hideLowRangeAuthors by booleanPref(key = "hideLowRangeAuthors", defaultValue = false)
    override var hideContentWithoutTags by booleanPref(key = "hideContentWithoutTags", defaultValue = false)
    override var hotEntriesScreen by stringPref(key = "hotEntriesScreen")
    override var defaultScreen by stringPref(key = "defaultScreen")
    override var fontSize by stringPref(key = "fontSize")
    override var linkImagePosition by stringPref(key = "linkImagePosition")
    override var linkShowImage by booleanPref(key = "linkShowImage", defaultValue = true)
    override var linkSimpleList by booleanPref(key = "linkSimpleList", defaultValue = false)
    override var linkShowAuthor by booleanPref(key = "linkShowAuthor", defaultValue = false)
    override var useDarkTheme by booleanPref(key = "useDarkTheme", defaultValue = false)
    override var useAmoledTheme by booleanPref(key = "useAmoledTheme", defaultValue = false)
    override var showMinifiedImages by booleanPref(key = "showMinifiedImages", defaultValue = false)
    override var piggyBackPushNotifications by booleanPref(key = "piggyBackPushNotifications", defaultValue = false)
    override var cutLongEntries by booleanPref(key = "cutLongEntries", defaultValue = true)
    override var cutImages by booleanPref(key = "cutImages", defaultValue = true)
    override var cutImageProportion by intPref(key = "cutImageProportion")
    override var openSpoilersDialog by booleanPref(key = "openSpoilersDialog", defaultValue = true)
    override var showNotifications by booleanPref(key = "showNotifications", defaultValue = true)
    override var hideLinkCommentsByDefault by booleanPref(key = "hideLinkCommentsByDefault", defaultValue = false)
    override var hideBlacklistedViews by booleanPref(key = "hideBlacklistedViews", defaultValue = false)
    override var enableEmbedPlayer by booleanPref(key = "enableEmbedPlayer", defaultValue = true)
    override var enableYoutubePlayer by booleanPref(key = "enableYoutubePlayer", defaultValue = true)
    override var useBuiltInBrowser by booleanPref(key = "useBuiltInBrowser", defaultValue = true)
    override var groupNotifications by booleanPref(key = "groupNotifications", defaultValue = true)
    override var disableExitConfirmation by booleanPref(key = "disableExitConfirmation", defaultValue = false)
    override var dialogShown by booleanPref(key = "dialogShown", defaultValue = false)
}
