package io.github.wykopmobilny.storage.android

import android.content.Context
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import javax.inject.Inject

internal class SettingsPreferences @Inject constructor(
    context: Context
) : Preferences(context, true), SettingsPreferencesApi {

    override var notificationsSchedulerDelay by stringPref(defaultValue = "15")
    override var showAdultContent by booleanPref(defaultValue = false)
    override var hideNsfw: Boolean by booleanPref(defaultValue = true)
    override var hideLowRangeAuthors: Boolean by booleanPref(defaultValue = false)
    override var hideContentWithoutTags: Boolean by booleanPref(defaultValue = false)
    override var hotEntriesScreen by stringPref(defaultValue = "newest")
    override var defaultScreen by stringPref(defaultValue = "mainpage")
    override var fontSize by stringPref(defaultValue = "normal")
    override var linkImagePosition by stringPref(defaultValue = "left")
    override var linkShowImage by booleanPref(defaultValue = true)
    override var linkSimpleList by booleanPref(defaultValue = false)
    override var linkShowAuthor by booleanPref(defaultValue = false)
    override var useDarkTheme by booleanPref(defaultValue = false)
    override var useAmoledTheme by booleanPref(defaultValue = false)
    override var showMinifiedImages by booleanPref(defaultValue = false)
    override var piggyBackPushNotifications by booleanPref(defaultValue = false)
    override var cutLongEntries by booleanPref(defaultValue = true)
    override var cutImages by booleanPref(defaultValue = true)
    override var cutImageProportion by intPref(defaultValue = 60)
    override var openSpoilersDialog by booleanPref(defaultValue = true)
    override var showNotifications by booleanPref(defaultValue = true)
    override var hideLinkCommentsByDefault by booleanPref(defaultValue = false)
    override var hideBlacklistedViews: Boolean by booleanPref(defaultValue = false)
    override var enableEmbedPlayer by booleanPref(defaultValue = true)
    override var enableYoutubePlayer by booleanPref(defaultValue = true)
    override var useBuiltInBrowser by booleanPref(defaultValue = true)
    override var groupNotifications by booleanPref(defaultValue = true)
    override var disableExitConfirmation by booleanPref(defaultValue = false)
    override var dialogShown by booleanPref(defaultValue = false)
}
