package io.github.wykopmobilny.domain.settings.prefs

import io.github.wykopmobilny.domain.settings.UserSettings
import io.github.wykopmobilny.domain.settings.get
import io.github.wykopmobilny.storage.api.UserPreferenceApi
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class GetMediaPreferences @Inject constructor(
    private val userPreferences: UserPreferenceApi,
) {

    operator fun invoke() = combine(
        userPreferences.get(UserSettings.useYoutubePlayer),
        userPreferences.get(UserSettings.useEmbeddedPlayer),
    ) { useYoutubePlayer, useEmbeddedPlayer ->
        MediaPlayerPreferences(
            useYoutubePlayer = useYoutubePlayer ?: true,
            useEmbeddedPlayer = useEmbeddedPlayer ?: true,
        )
    }
}

data class MediaPlayerPreferences(
    val useYoutubePlayer: Boolean,
    val useEmbeddedPlayer: Boolean,
)
