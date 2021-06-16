package io.github.wykopmobilny.storage.api

import kotlinx.coroutines.flow.Flow

interface SettingsPreferencesApi {
    var notificationsSchedulerDelay: String?
    var hotEntriesScreen: String?
    var defaultScreen: String?
    var linkImagePosition: String?
    var linkShowImage: Boolean
    var linkSimpleList: Boolean
    var linkShowAuthor: Boolean
    var showAdultContent: Boolean
    var hideNsfw: Boolean
    var useDarkTheme: Boolean
    var useAmoledTheme: Boolean
    var showNotifications: Boolean
    var piggyBackPushNotifications: Boolean
    var showMinifiedImages: Boolean
    var cutLongEntries: Boolean
    var cutImages: Boolean
    var openSpoilersDialog: Boolean
    var hideLowRangeAuthors: Boolean
    var hideContentWithoutTags: Boolean
    var cutImageProportion: Int?
    var fontSize: String?
    var hideLinkCommentsByDefault: Boolean
    var hideBlacklistedViews: Boolean
    var enableYoutubePlayer: Boolean
    var enableEmbedPlayer: Boolean
    var useBuiltInBrowser: Boolean
    var groupNotifications: Boolean
    var disableExitConfirmation: Boolean

    val theme: Flow<AppTheme?>

    suspend fun updateTheme(newValue: AppTheme?)
}

enum class AppTheme {
    Light,
    Dark,
    DarkAmoled,
}
