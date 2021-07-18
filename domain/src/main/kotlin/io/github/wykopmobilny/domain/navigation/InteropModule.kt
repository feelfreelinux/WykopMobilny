package io.github.wykopmobilny.domain.navigation

import dagger.Binds
import dagger.Module
import io.github.wykopmobilny.domain.settings.FontSize
import io.github.wykopmobilny.domain.settings.LinkImagePosition
import io.github.wykopmobilny.domain.settings.UserSetting
import io.github.wykopmobilny.domain.settings.UserSettings
import io.github.wykopmobilny.domain.settings.get
import io.github.wykopmobilny.domain.settings.prefs.MainScreen
import io.github.wykopmobilny.domain.settings.prefs.MikroblogScreen
import io.github.wykopmobilny.domain.settings.update
import io.github.wykopmobilny.domain.styles.GetAppTheme
import io.github.wykopmobilny.storage.api.SettingsPreferencesApi
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Module
internal abstract class InteropModule {

    @Binds
    abstract fun InMemoryInteropRequestService.navigationRequestsController(): InteropRequestsProvider

    @Binds
    abstract fun InMemoryInteropRequestService.navigationRequestsInterop(): InteropRequestService

    @Binds
    abstract fun InteropSettingPreferencesApi.settingsPreferencesApi(): SettingsPreferencesApi
}

@Singleton
internal class InteropSettingPreferencesApi @Inject constructor(
    private val userPreferenceApi: UserPreferenceApi,
    private val getAppTheme: GetAppTheme,
) : SettingsPreferencesApi {

    override val hotEntriesScreen: String?
        get() = when (interop(UserSettings.mikroblogScreen)) {
            MikroblogScreen.Active -> "active"
            MikroblogScreen.Newest -> "newest"
            MikroblogScreen.SixHours -> "6"
            MikroblogScreen.TwelveHours -> "12"
            MikroblogScreen.TwentyFourHours -> "24"
            null -> null
        }
    override val defaultScreen: String?
        get() = when (interop(UserSettings.defaultScreen)) {
            MainScreen.Promoted -> "mainpage"
            MainScreen.Mikroblog -> "mikroblog"
            MainScreen.MyWykop -> "mywykop"
            MainScreen.Hits -> "hits"
            null -> null
        }
    override val linkImagePosition: String?
        get() = when (interop(UserSettings.imagePosition)) {
            LinkImagePosition.Left -> "left"
            LinkImagePosition.Right -> "right"
            LinkImagePosition.Top -> "top"
            LinkImagePosition.Bottom -> "bottom"
            null -> null
        }
    override val linkShowImage: Boolean
        get() = interop(UserSettings.showLinkThumbnail) ?: true
    override val linkSimpleList: Boolean
        get() = interop(UserSettings.useSimpleList) ?: false
    override val linkShowAuthor: Boolean
        get() = interop(UserSettings.showAuthor) ?: false
    override val showAdultContent: Boolean
        get() = (interop(UserSettings.hidePlus18Content) ?: true).not()
    override val hideNsfw: Boolean
        get() = interop(UserSettings.hideNsfwContent) ?: true
    override val showMinifiedImages: Boolean
        get() = interop(UserSettings.showMinifiedImages) ?: false
    override val cutLongEntries: Boolean
        get() = interop(UserSettings.cutLongEntries) ?: true
    override val cutImages: Boolean
        get() = interop(UserSettings.cutImages) ?: true
    override val openSpoilersDialog: Boolean
        get() = interop(UserSettings.openSpoilersInDialog) ?: true
    override val hideLowRangeAuthors: Boolean
        get() = interop(UserSettings.showAuthor) ?: false
    override val hideContentWithoutTags: Boolean
        get() = interop(UserSettings.hideContentWithNoTags) ?: false
    override val cutImageProportion: Int?
        get() = interop(UserSettings.cutImagesProportion)?.toInt()
    override val fontSize: String
        get() = when (interop(UserSettings.font)) {
            FontSize.VerySmall -> "tiny"
            FontSize.Small -> "small"
            FontSize.Normal -> "normal"
            FontSize.Large -> "large"
            FontSize.VeryLarge -> "huge"
            null -> "normal"
        }
    override val hideLinkCommentsByDefault: Boolean
        get() = interop(UserSettings.cutLinkComments) ?: false
    override val hideBlacklistedViews: Boolean
        get() = interop(UserSettings.hideBlacklistedContent) ?: false
    override val enableYoutubePlayer: Boolean
        get() = interop(UserSettings.useYoutubePlayer) ?: true
    override val enableEmbedPlayer: Boolean
        get() = interop(UserSettings.useEmbeddedPlayer) ?: true
    override val useBuiltInBrowser: Boolean
        get() = interop(UserSettings.useEmbeddedBrowser) ?: true
    override var groupNotifications: Boolean
        get() = interop(UserSettings.groupNotifications) ?: true
        set(value) {
            runBlocking { userPreferenceApi.update(UserSettings.groupNotifications, value) }
        }
    override val disableExitConfirmation: Boolean
        get() = interop(UserSettings.exitConfirmation) ?: true

    private fun <T : Enum<T>> interop(setting: UserSetting<T>): T? =
        runBlocking {
            userPreferenceApi.get(setting).first()
        }

    @JvmName("interopBoolean")
    private fun interop(setting: UserSetting<Boolean>): Boolean? =
        runBlocking {
            userPreferenceApi.get(setting).first()
        }

    @JvmName("interopLong")
    private fun interop(setting: UserSetting<Long>): Long? =
        runBlocking {
            userPreferenceApi.get(setting).first()
        }
}
