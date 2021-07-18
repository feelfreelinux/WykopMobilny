package io.github.wykopmobilny.domain.settings.prefs

import io.github.wykopmobilny.domain.settings.UserSettings
import io.github.wykopmobilny.domain.settings.get
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

internal class GetFilteringPreferences @Inject constructor(
    private val userPreferences: UserPreferenceApi,
) {

    operator fun invoke() = combine(
        userPreferences.get(UserSettings.hidePlus18Content),
        userPreferences.get(UserSettings.hideNsfwContent),
        userPreferences.get(UserSettings.hideNewUserContent),
        userPreferences.get(UserSettings.hideContentWithNoTags),
        userPreferences.get(UserSettings.hideBlacklistedContent),
        userPreferences.get(UserSettings.useEmbeddedBrowser),
    ) { items ->
        @Suppress("MagicNumber")
        FilteringPreferences(
            hidePlus18Content = items[0] ?: true,
            hideNsfwContent = items[1] ?: true,
            hideNewUserContent = items[2] ?: false,
            hideContentWithNoTags = items[3] ?: false,
            hideBlacklistedContent = items[4] ?: false,
            useEmbeddedBrowser = items[5] ?: true,
        )
    }
        .distinctUntilChanged()
}

internal data class FilteringPreferences(
    val hidePlus18Content: Boolean,
    val hideNsfwContent: Boolean,
    val hideNewUserContent: Boolean,
    val hideContentWithNoTags: Boolean,
    val hideBlacklistedContent: Boolean,
    val useEmbeddedBrowser: Boolean,
)
