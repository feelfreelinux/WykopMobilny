package io.github.wykopmobilny.domain.settings

import io.github.wykopmobilny.domain.settings.di.SettingsScope
import io.github.wykopmobilny.domain.settings.prefs.GetAppearanceSectionPreferences
import io.github.wykopmobilny.domain.settings.prefs.GetImagesPreferences
import io.github.wykopmobilny.domain.settings.prefs.GetImagesPreferences.Companion.CUT_IMAGES_RANGE_FROM
import io.github.wykopmobilny.domain.settings.prefs.GetImagesPreferences.Companion.CUT_IMAGES_RANGE_TO
import io.github.wykopmobilny.domain.settings.prefs.GetLinksPreferences
import io.github.wykopmobilny.domain.settings.prefs.GetMediaPreferences
import io.github.wykopmobilny.domain.settings.prefs.GetMikroblogPreferences
import io.github.wykopmobilny.domain.settings.prefs.MainScreen
import io.github.wykopmobilny.domain.settings.prefs.MikroblogScreen
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import io.github.wykopmobilny.ui.base.AppScopes
import io.github.wykopmobilny.ui.base.launchIn
import io.github.wykopmobilny.ui.settings.AppearancePreferencesUi
import io.github.wykopmobilny.ui.settings.AppearancePreferencesUi.AppearanceSectionUi
import io.github.wykopmobilny.ui.settings.AppearancePreferencesUi.ImagesSectionUi
import io.github.wykopmobilny.ui.settings.AppearancePreferencesUi.LinksSectionUi
import io.github.wykopmobilny.ui.settings.AppearancePreferencesUi.MediaPlayerSectionUi
import io.github.wykopmobilny.ui.settings.AppearancePreferencesUi.MikroblogSectionUi
import io.github.wykopmobilny.ui.settings.FontSizeUi
import io.github.wykopmobilny.ui.settings.GetAppearancePreferences
import io.github.wykopmobilny.ui.settings.LinkImagePositionUi
import io.github.wykopmobilny.ui.settings.ListSetting
import io.github.wykopmobilny.ui.settings.MainScreenUi
import io.github.wykopmobilny.ui.settings.MikroblogScreenUi
import io.github.wykopmobilny.ui.settings.Setting
import io.github.wykopmobilny.ui.settings.SliderSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAppearancePreferencesQuery @Inject internal constructor(
    private val userPreferences: UserPreferenceApi,
    private val getMediaPreferences: GetMediaPreferences,
    private val getMikroblogPreferences: GetMikroblogPreferences,
    private val getAppearanceSectionPreferences: GetAppearanceSectionPreferences,
    private val getLinksPreferences: GetLinksPreferences,
    private val getImagesPreferences: GetImagesPreferences,
    private val appScopes: AppScopes,
) : GetAppearancePreferences {

    override fun invoke() =
        combine(
            appearanceFlow(),
            mediaPlayerFlow(),
            mikroblogFlow(),
            linksFlow(),
            imagesFlow(),
        ) { appearance, mediaPlayer, mikroblog, links, images ->
            AppearancePreferencesUi(
                appearance = appearance,
                mediaPlayerSection = mediaPlayer,
                mikroblogSection = mikroblog,
                linksSection = links,
                imagesSection = images,
            )
        }

    private fun appearanceFlow(): Flow<AppearanceSectionUi> =
        getAppearanceSectionPreferences().map { appearance ->
            AppearanceSectionUi(
                useDarkTheme = Setting(
                    currentValue = appearance.isDarkTheme,
                    onClicked = { updateUserSetting(UserSettings.darkTheme, !appearance.isDarkTheme) },
                ),
                useAmoledTheme = Setting(
                    currentValue = appearance.isAmoledTheme,
                    isEnabled = appearance.isDarkTheme,
                    onClicked = { updateUserSetting(UserSettings.useAmoledTheme, !appearance.isAmoledTheme) },
                ),
                fontSize = ListSetting(
                    values = FontSizeUi.values().toList(),
                    currentValue = appearance.defaultFont.toUi(),
                    onSelected = { updateUserSetting(UserSettings.font, it.toDomain()) },
                ),
                startScreen = ListSetting(
                    values = MainScreenUi.values().toList(),
                    currentValue = appearance.defaultScreen.toUi(),
                    onSelected = { updateUserSetting(UserSettings.defaultScreen, it.toDomain()) },
                ),
            )
        }

    private fun mediaPlayerFlow() =
        getMediaPreferences().map {
            MediaPlayerSectionUi(
                enableYoutubePlayer = Setting(
                    currentValue = it.useYoutubePlayer,
                    onClicked = { updateUserSetting(UserSettings.useYoutubePlayer, !it.useYoutubePlayer) },
                ),
                enableEmbedPlayer = Setting(
                    currentValue = it.useEmbeddedPlayer,
                    onClicked = { updateUserSetting(UserSettings.useEmbeddedPlayer, !it.useEmbeddedPlayer) },
                ),
            )
        }

    private fun mikroblogFlow() =
        getMikroblogPreferences().map {
            MikroblogSectionUi(
                mikroblogScreen = ListSetting(
                    values = MikroblogScreenUi.values().toList(),
                    currentValue = it.defaultScreen.toUi(),
                    onSelected = { updateUserSetting(UserSettings.mikroblogScreen, it.toDomain()) },
                ),
                cutLongEntries = Setting(
                    currentValue = it.cutLongEntries,
                    onClicked = { updateUserSetting(UserSettings.cutLongEntries, !it.cutLongEntries) },
                ),
                openSpoilersInDialog = Setting(
                    currentValue = it.openSpoilersInDialog,
                    onClicked = { updateUserSetting(UserSettings.openSpoilersInDialog, !it.openSpoilersInDialog) },
                ),
            )
        }

    private fun linksFlow() =
        getLinksPreferences().map {
            LinksSectionUi(
                useSimpleList = Setting(
                    currentValue = it.useSimpleList,
                    onClicked = { updateUserSetting(UserSettings.useSimpleList, !it.useSimpleList) },
                ),
                showLinkThumbnail = Setting(
                    currentValue = it.showLinkThumbnail,
                    onClicked = { updateUserSetting(UserSettings.showLinkThumbnail, !it.showLinkThumbnail) },
                ),
                imagePosition = ListSetting(
                    values = LinkImagePositionUi.values().toList(),
                    isEnabled = it.showLinkThumbnail && !it.useSimpleList,
                    currentValue = it.imagePosition.toUi(),
                    onSelected = { updateUserSetting(UserSettings.imagePosition, it.toDomain()) },
                ),
                showAuthor = Setting(
                    currentValue = it.showAuthor,
                    isEnabled = it.showLinkThumbnail && !it.useSimpleList,
                    onClicked = { updateUserSetting(UserSettings.showAuthor, !it.showAuthor) },
                ),
                cutLinkComments = Setting(
                    currentValue = it.cutLinkComments,
                    onClicked = { updateUserSetting(UserSettings.cutLinkComments, !it.cutLinkComments) },
                ),
            )
        }

    private fun imagesFlow() =
        getImagesPreferences().map {
            ImagesSectionUi(
                showMinifiedImages = Setting(
                    currentValue = it.showMinifiedImages,
                    onClicked = { updateUserSetting(UserSettings.showMinifiedImages, !it.showMinifiedImages) },
                ),
                cutImages = Setting(
                    currentValue = it.cutImages,
                    onClicked = { updateUserSetting(UserSettings.cutImages, !it.cutImages) },
                ),
                cutImagesProportion = SliderSetting(
                    values = CUT_IMAGES_RANGE_FROM..CUT_IMAGES_RANGE_TO,
                    currentValue = it.cutImagesProportion,
                    isEnabled = it.cutImages,
                    onChanged = { updateUserSetting(UserSettings.cutImagesProportion, it.toLong()) },
                ),
            )
        }

    private fun <T> updateUserSetting(key: UserSetting<T>, value: T) {
        appScopes.launchIn<SettingsScope> { userPreferences.update(key, value) }
    }
}

private fun MainScreen.toUi() =
    when (this) {
        MainScreen.Promoted -> MainScreenUi.Promoted
        MainScreen.Mikroblog -> MainScreenUi.Mikroblog
        MainScreen.MyWykop -> MainScreenUi.MyWykop
        MainScreen.Hits -> MainScreenUi.Hits
    }

private fun MainScreenUi.toDomain() =
    when (this) {
        MainScreenUi.Promoted -> MainScreen.Promoted
        MainScreenUi.Mikroblog -> MainScreen.Mikroblog
        MainScreenUi.MyWykop -> MainScreen.MyWykop
        MainScreenUi.Hits -> MainScreen.Hits
    }

private fun MikroblogScreenUi.toDomain() =
    when (this) {
        MikroblogScreenUi.Active -> MikroblogScreen.Active
        MikroblogScreenUi.Newest -> MikroblogScreen.Newest
        MikroblogScreenUi.SixHours -> MikroblogScreen.SixHours
        MikroblogScreenUi.TwelveHours -> MikroblogScreen.TwelveHours
        MikroblogScreenUi.TwentyFourHours -> MikroblogScreen.TwentyFourHours
    }

private fun MikroblogScreen.toUi() =
    when (this) {
        MikroblogScreen.Active -> MikroblogScreenUi.Active
        MikroblogScreen.Newest -> MikroblogScreenUi.Newest
        MikroblogScreen.SixHours -> MikroblogScreenUi.SixHours
        MikroblogScreen.TwelveHours -> MikroblogScreenUi.TwelveHours
        MikroblogScreen.TwentyFourHours -> MikroblogScreenUi.TwentyFourHours
    }

private fun FontSizeUi.toDomain() =
    when (this) {
        FontSizeUi.VerySmall -> FontSize.VerySmall
        FontSizeUi.Small -> FontSize.Small
        FontSizeUi.Normal -> FontSize.Normal
        FontSizeUi.Large -> FontSize.Large
        FontSizeUi.VeryLarge -> FontSize.VeryLarge
    }

private fun FontSize.toUi() =
    when (this) {
        FontSize.VerySmall -> FontSizeUi.VerySmall
        FontSize.Small -> FontSizeUi.Small
        FontSize.Normal -> FontSizeUi.Normal
        FontSize.Large -> FontSizeUi.Large
        FontSize.VeryLarge -> FontSizeUi.VeryLarge
    }

private fun LinkImagePositionUi.toDomain() =
    when (this) {
        LinkImagePositionUi.Left -> LinkImagePosition.Left
        LinkImagePositionUi.Right -> LinkImagePosition.Right
        LinkImagePositionUi.Top -> LinkImagePosition.Top
        LinkImagePositionUi.Bottom -> LinkImagePosition.Bottom
    }

private fun LinkImagePosition.toUi() =
    when (this) {
        LinkImagePosition.Left -> LinkImagePositionUi.Left
        LinkImagePosition.Right -> LinkImagePositionUi.Right
        LinkImagePosition.Top -> LinkImagePositionUi.Top
        LinkImagePosition.Bottom -> LinkImagePositionUi.Bottom
    }
