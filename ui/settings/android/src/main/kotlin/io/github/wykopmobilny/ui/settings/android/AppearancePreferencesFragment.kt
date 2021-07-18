package io.github.wykopmobilny.ui.settings.android

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import io.github.wykopmobilny.ui.settings.FontSizeUi
import io.github.wykopmobilny.ui.settings.GetAppearancePreferences
import io.github.wykopmobilny.ui.settings.LinkImagePositionUi
import io.github.wykopmobilny.ui.settings.MainScreenUi
import io.github.wykopmobilny.ui.settings.MikroblogScreenUi
import io.github.wykopmobilny.ui.settings.android.di.DaggerSettingsUiComponent
import io.github.wykopmobilny.utils.requireDependency
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

internal class AppearancePreferencesFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var getAppearancePreferences: GetAppearancePreferences

    override fun onAttach(context: Context) {
        DaggerSettingsUiComponent.factory()
            .create(
                deps = context.requireDependency(),
            )
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appearance_preferences, rootKey)

        lifecycleScope.launchWhenResumed {
            getAppearancePreferences().collect {
                bindCheckbox("useDarkTheme", it.appearance.useDarkTheme)
                bindCheckbox("useAmoledTheme", it.appearance.useAmoledTheme)
                bindList("defaultScreen", it.appearance.startScreen, defaultScreenMapping)
                bindList("fontSize", it.appearance.fontSize, fontMapping)
                bindCheckbox("enableYoutubePlayer", it.mediaPlayerSection.enableYoutubePlayer)
                bindCheckbox("enableEmbedPlayer", it.mediaPlayerSection.enableEmbedPlayer)
                bindList("hotEntriesScreen", it.mikroblogSection.mikroblogScreen, defaultMikroblogScreenMapping)
                bindCheckbox("cutLongEntries", it.mikroblogSection.cutLongEntries)
                bindCheckbox("openSpoilersDialog", it.mikroblogSection.openSpoilersInDialog)
                bindCheckbox("linkSimpleList", it.linksSection.useSimpleList)
                bindCheckbox("linkShowImage", it.linksSection.showLinkThumbnail)
                bindList("linkImagePosition", it.linksSection.imagePosition, imagePositionMapping)
                bindCheckbox("linkShowAuthor", it.linksSection.showAuthor)
                bindCheckbox("hideLinkCommentsByDefault", it.linksSection.cutLinkComments)
                bindCheckbox("showMinifiedImages", it.imagesSection.showMinifiedImages)
                bindCheckbox("cutImages", it.imagesSection.cutImages)
                bindSlider("cutImageProportion", it.imagesSection.cutImagesProportion)
            }
        }
    }

    private val defaultScreenMapping by lazy {
        MainScreenUi.values().associateWith { screen ->
            when (screen) {
                MainScreenUi.Promoted -> R.string.main_page
                MainScreenUi.Mikroblog -> R.string.mikroblog
                MainScreenUi.MyWykop -> R.string.mywykop
                MainScreenUi.Hits -> R.string.hits
            }
                .let { resources.getString(it) }
        }
    }

    private val fontMapping by lazy {
        FontSizeUi.values().associateWith { font ->
            when (font) {
                FontSizeUi.VerySmall -> R.string.fontsize_tiny
                FontSizeUi.Small -> R.string.fontsize_small
                FontSizeUi.Normal -> R.string.fontsize_normal
                FontSizeUi.Large -> R.string.fontsize_large
                FontSizeUi.VeryLarge -> R.string.fontsize_huge
            }
                .let { resources.getString(it) }
        }
    }

    private val imagePositionMapping by lazy {
        LinkImagePositionUi.values().associateWith { position ->
            when (position) {
                LinkImagePositionUi.Left -> R.string.link_image_position_left
                LinkImagePositionUi.Right -> R.string.link_image_position_right
                LinkImagePositionUi.Top -> R.string.link_image_position_top
                LinkImagePositionUi.Bottom -> R.string.link_image_position_bottom
            }
                .let { resources.getString(it) }
        }
    }

    private val defaultMikroblogScreenMapping by lazy {
        MikroblogScreenUi.values().associateWith {
            when (it) {
                MikroblogScreenUi.Active -> R.string.active_entries
                MikroblogScreenUi.Newest -> R.string.newest_entries
                MikroblogScreenUi.SixHours -> R.string.period6
                MikroblogScreenUi.TwelveHours -> R.string.period12
                MikroblogScreenUi.TwentyFourHours -> R.string.period24
            }
                .let { resources.getString(it) }
        }
    }
}
