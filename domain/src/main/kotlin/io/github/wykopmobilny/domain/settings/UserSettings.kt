package io.github.wykopmobilny.domain.settings

import io.github.wykopmobilny.domain.settings.prefs.MainScreen
import io.github.wykopmobilny.domain.settings.prefs.MikroblogScreen

internal object UserSettings {

    val darkTheme = booleanMapping(preferencesKey = "settings.appearance.dark_theme")
    val useAmoledTheme = booleanMapping(preferencesKey = "settings.appearance.amoled_theme")
    val defaultScreen = enumMapping(
        preferencesKey = "settings.appearance.default_screen",
        enumMapping = mapOf(
            MainScreen.Promoted to "promoted",
            MainScreen.Mikroblog to "mikroblog",
            MainScreen.MyWykop to "my_wykop",
            MainScreen.Hits to "hits",
        ),
    )
    val font = enumMapping(
        preferencesKey = "settings.appearance.font_size",
        enumMapping = mapOf(
            FontSize.VerySmall to "very_small",
            FontSize.Small to "small",
            FontSize.Normal to "normal",
            FontSize.Large to "large",
            FontSize.VeryLarge to "very_large",
        ),
    )

    val notificationsEnabled = booleanMapping(preferencesKey = "settings.notifications.enabled")
    val notificationsRefreshPeriod = durationMapping(preferencesKey = "settings.notifications.refresh_period")

    val exitConfirmation = booleanMapping(preferencesKey = "settings.filtering.exit_confirmation")
    val hidePlus18Content = booleanMapping(preferencesKey = "settings.filtering.hide_plus_18_content")
    val hideNsfwContent = booleanMapping(preferencesKey = "settings.filtering.hide_nsfw_content")
    val hideNewUserContent = booleanMapping(preferencesKey = "settings.filtering.hide_new_user_content")
    val hideContentWithNoTags = booleanMapping(preferencesKey = "settings.filtering.hide_content_with_no_tags")
    val hideBlacklistedContent = booleanMapping(preferencesKey = "settings.filtering.hide_blacklisted_content")
    val useEmbeddedBrowser = booleanMapping(preferencesKey = "settings.filtering.use_embedded_browser")

    val useYoutubePlayer = booleanMapping(preferencesKey = "settings.media.use_youtube_player")
    val useEmbeddedPlayer = booleanMapping(preferencesKey = "settings.media.use_embedded_player")

    val mikroblogScreen = enumMapping(
        preferencesKey = "settings.mikroblog.default_screen",
        enumMapping = mapOf(
            MikroblogScreen.Active to "active",
            MikroblogScreen.Newest to "newest",
            MikroblogScreen.SixHours to "six-hours",
            MikroblogScreen.TwelveHours to "twelve-hours",
            MikroblogScreen.TwentyFourHours to "twenty-four-hours",
        ),
    )
    val cutLongEntries = booleanMapping(preferencesKey = "settings.mikroblog.cut_long_entries")
    val openSpoilersInDialog = booleanMapping(preferencesKey = "settings.mikroblog.open_spoilers_in_dialog")

    val imagePosition = enumMapping(
        preferencesKey = "settings.links.image_position",
        enumMapping = mapOf(
            LinkImagePosition.Left to "left",
            LinkImagePosition.Right to "right",
            LinkImagePosition.Top to "top",
            LinkImagePosition.Bottom to "bottom",
        ),
    )
    val useSimpleList = booleanMapping(preferencesKey = "settings.links.use_simple_list")
    val showLinkThumbnail = booleanMapping(preferencesKey = "settings.links.show_thumbnails")
    val showAuthor = booleanMapping(preferencesKey = "settings.links.show_author")
    val cutLinkComments = booleanMapping(preferencesKey = "settings.links.cut_link_comments")

    val showMinifiedImages = booleanMapping("settings.images.show_minified_images")
    val cutImages = booleanMapping("settings.images.cut_images")
    val cutImagesProportion = longMapping("settings.images.cut_images_proportion")

    val groupNotifications = booleanMapping("settings.in_app_notifications.enabled_grouping")
}
