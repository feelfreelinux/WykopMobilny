package io.github.wykopmobilny.storage.api

interface SettingsPreferencesApi {
    val hotEntriesScreen: String?
    val defaultScreen: String?
    val linkImagePosition: String?
    val linkShowImage: Boolean
    val linkSimpleList: Boolean
    val linkShowAuthor: Boolean
    val showAdultContent: Boolean
    val hideNsfw: Boolean
    val showMinifiedImages: Boolean
    val cutLongEntries: Boolean
    val cutImages: Boolean
    val openSpoilersDialog: Boolean
    val hideLowRangeAuthors: Boolean
    val hideContentWithoutTags: Boolean
    val cutImageProportion: Int?
    val fontSize: String?
    val hideLinkCommentsByDefault: Boolean
    val hideBlacklistedViews: Boolean
    val enableYoutubePlayer: Boolean
    val enableEmbedPlayer: Boolean
    val useBuiltInBrowser: Boolean
    var groupNotifications: Boolean
    val disableExitConfirmation: Boolean
}
